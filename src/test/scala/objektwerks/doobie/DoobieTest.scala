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
  val db = Transactor.fromDriverManager[IO]( "org.h2.Driver", "jdbc:h2:./target/testdb", "", "" )
  val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString

  test("ddl > insert > update > select") {
    assert(ddl(schema) == 0)
    assert(insert == 2)
    assert(update == 1)
    assert(select == 4)
  }

  def ddl(schema: String): Int = {
    val ddl = Fragment.const(schema).update.run
    ddl.transact(db).unsafeRunSync()
  }

  def insert: Int = {
    val barney = Fragment.const("insert into worker(name) values ('barney')").update
    val fred = Fragment.const("insert into worker(name) values('fred')").update
    val workers = (barney.run *> fred.run).transact(db).unsafeRunSync()

    val barneyTask = Fragment.const("insert into task(workerId, task) values(1, 'clean pool')").update
    val fredTask = Fragment.const("insert into task(workerId, task) values(2, 'clean pool')").update
    val tasks = (barneyTask.run *> fredTask.run).transact(db).unsafeRunSync()

    workers + tasks
  }

  def update: Int = {
    val barneyUpdate = Fragment.const("update worker set name = 'barney rebel' where name = 'barney'").update
    val fredUpdate = Fragment.const("update worker set name = 'fred flintstone' where name = 'fred'").update
    (barneyUpdate.run *> fredUpdate.run).transact(db).unsafeRunSync()
  }

  def select: Int = {
    val selectWorkers = Fragment.const("select * from worker").query[Worker]
    val selectTasks = Fragment.const("select * from task").query[Task]
    val workers = selectWorkers.to[List].transact(db).unsafeRunSync.length
    val tasks = selectTasks.to[List].transact(db).unsafeRunSync.length
    workers + tasks
  }
}