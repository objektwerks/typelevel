package objektwerks.doobie

import cats.effect.{IO, _}
import cats.implicits._
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

  val insertWorker = Update[String]("insert into worker(name) values(?)")
  val insertTask = Update[(Int, String)]("insert into task(workerId, task) values(?, ?)")

  val updateWorker = Update[(String, String)]("update worker set name = ? where name = ?")

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
    check(insertWorker)
    check(insertTask)
    check(updateWorker)
    check(selectWorkers)
    check(selectTasks)
    check(deleteWorkers)
    check(deleteTasks)
  }

  def ddl(schema: String): Int = Fragment.const(schema).update.run.transact(xa).unsafeRunSync

  def insert: (Int, Int, Int, Int) = {
    val barneyId = insertWorker.toUpdate0("barney")
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredId =insertWorker.toUpdate0("fred")
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val barneyTaskId = insertTask.toUpdate0((barneyId, "clean pool"))
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    val fredTaskId = insertTask.toUpdate0((fredId, "clean car"))
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
      .unsafeRunSync
    (barneyId, fredId, barneyTaskId, fredTaskId)
  }

  def update: Int = (updateWorker.toUpdate0(("barney rebel", "barney"))
    .run *> updateWorker.toUpdate0(("fred flintstone", "fred")).run)
    .transact(xa)
    .unsafeRunSync

  def select: Int = {
    val workers = selectWorkers.to[List].transact(xa).unsafeRunSync
    val tasks = selectTasks.to[List].transact(xa).unsafeRunSync
    workers.length + tasks.length
  }

  def delete: Int = (deleteTasks.run *> deleteWorkers.run).transact(xa).unsafeRunSync
}