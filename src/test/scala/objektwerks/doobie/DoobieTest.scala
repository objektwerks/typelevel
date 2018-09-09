package objektwerks.doobie

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._

import org.scalatest.FunSuite

import scala.io.Source

case class Worker(id: Int = 0, name: String)

case class Task(id: Int = 0, workerId: Int, task: String)

class DoobieTest extends FunSuite {
  val xa = Transactor.fromDriverManager[IO]( "org.h2.Driver", "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", "sa" )
  val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString

  test("ddl > insert > update > select") {
    assert(ddl(schema) == 0)
    assert((1, 2, 1, 2) == insert)
    assert(update == 1)
    assert(select == 4)
    assert(delete == 4)
  }

  def ddl(schema: String): Int = {
    val ddl = Fragment.const(schema).update.run
    ddl.transact(xa).unsafeRunSync
  }

  def insert: (Int, Int, Int, Int) = {
    val barneyId = sql"insert into worker(name) values ('barney')"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredId = sql"insert into worker(name) values('fred')"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync

    val barneyTaskId = sql"insert into task(workerId, task) values($barneyId, 'clean pool')"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredTaskId = sql"insert into task(workerId, task) values($fredId, 'clean pool')"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync

    (barneyId, fredId, barneyTaskId, fredTaskId)
  }

  def update: Int = {
    val barneyUpdate = sql"update worker set name = 'barney rebel' where name = 'barney'".update
    val fredUpdate = sql"update worker set name = 'fred flintstone' where name = 'fred'".update
    (barneyUpdate.run *> fredUpdate.run).transact(xa).unsafeRunSync
  }

  def select: Int = {
    val workers = sql"select * from worker".query[Worker].to[List].transact(xa).unsafeRunSync
    val tasks = sql"select * from task".query[Task].to[List].transact(xa).unsafeRunSync

    workers.foreach(println)
    tasks.foreach(println)

    workers.length + tasks.length
  }

  def delete: Int = {
    val deletedTasks = sql"delete from task".update.run.transact(xa).unsafeRunSync
    val deletedWorkers = sql"delete from worker".update.run.transact(xa).unsafeRunSync
    deletedTasks + deletedWorkers
  }
}