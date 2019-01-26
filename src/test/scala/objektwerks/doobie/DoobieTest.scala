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

  val insertBarney = sql"insert into worker(name) values ('barney')".update
  val insertFred = sql"insert into worker(name) values('fred')".update
  val updateBarney = sql"update worker set name = 'barney rebel' where name = 'barney'".update
  val updateFred = sql"update worker set name = 'fred flintstone' where name = 'fred'".update
  val selectWorkers = sql"select * from worker".query[Worker]
  val selectTasks = sql"select * from task".query[Task]
  val deleteTasks = sql"delete from task".update
  val deleteWorkers = sql"delete from worker".update

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
    check(insertBarney)
    check(insertFred)
    check(selectWorkers)
    check(selectTasks)
    check(deleteWorkers)
    check(deleteTasks)
  }

  def ddl(schema: String): Int = Fragment.const(schema).update.run.transact(xa).unsafeRunSync

  def insert: (Int, Int, Int, Int) = {
    val barneyId = insertBarney
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredId =insertFred
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val insertBarneyTask = sql"insert into task(workerId, task) values($barneyId, 'clean pool')".update
    val insertFredTask = sql"insert into task(workerId, task) values($fredId, 'clean pool')".update
    check(insertBarneyTask)
    check(insertFredTask)
    val barneyTaskId = insertBarneyTask
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredTaskId = insertFredTask
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    (barneyId, fredId, barneyTaskId, fredTaskId)
  }

  def update: Int = (updateBarney.run *> updateFred.run).transact(xa).unsafeRunSync

  def select: Int = {
    val workers = selectWorkers.to[List].transact(xa).unsafeRunSync
    val tasks = selectTasks.to[List].transact(xa).unsafeRunSync
    workers.length + tasks.length
  }

  def delete: Int = (deleteTasks.run *> deleteWorkers.run).transact(xa).unsafeRunSync
}