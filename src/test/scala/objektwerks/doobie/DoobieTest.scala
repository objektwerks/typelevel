package objektwerks.doobie

import cats.effect._
import cats.implicits._
import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.scalatest._
import doobie.util.transactor.Transactor
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext
import scala.io.Source

case class Worker(id: Int = 0, name: String)
case class Task(id: Int = 0, workerId: Int, task: String)

class DoobieTest extends FunSuite with IOChecker {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val xa = Transactor.fromDriverManager[IO]( "org.h2.Driver", "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", "sa" )
  val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString

  override def transactor: Transactor[IO] = xa

  test("ddl") {
    assert(ddl(schema) == 0)
  }

  test("insert") {
    assert((1, 2, 1, 2) == insert)
  }

  test("update") {
    assert(update == 1)
  }

  test("select") {
    assert(select == 4)
  }

  test("delete") {
    assert(delete == 2)
  }

  test("check") {
    val workers = sql"select * from worker".query[Worker]
    val tasks = sql"select * from task".query[Task]
    check(workers)
    check(tasks)
  }

  def ddl(schema: String): Int = Fragment.const(schema).update.run.transact(xa).unsafeRunSync

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
    workers.length + tasks.length
  }

  def delete: Int = {
    val deleteTasks = sql"delete from task".update
    val deleteWorkers = sql"delete from worker".update
    (deleteTasks.run *> deleteWorkers.run).transact(xa).unsafeRunSync
  }
}