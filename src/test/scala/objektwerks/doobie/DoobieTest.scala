package objektwerks.doobie

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

case class Worker(id: Int = 0, name: String)

case class Task(id: Int = 0, workerId: Int, task: String)

class DoobieTest extends FunSuite with Matchers {
  private val db = Transactor.fromDriverManager[IO]( "org.h2.Driver", "jdbc:h2:./target/testdb", "", "" )

  test("ddl > insert > update > select") {
    val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString
    val ddl = Fragment.const(schema).update
    ddl.run.transact(db).unsafeRunSync()

    val barney = Fragment.const("insert into worker(name) values ('barney')").update
    val fred = Fragment.const("insert into worker(name) values('fred')").update
    (barney.run *> fred.run).transact(db).unsafeRunSync()

    val barneyTask = Fragment.const("insert into task(workerId, task) values(1, 'clean pool')").update
    val fredTask = Fragment.const("insert into task(workerId, task) values(2, 'clean pool')").update
    (barneyTask.run *> fredTask.run).transact(db).unsafeRunSync()

    val barneyUpdate = Fragment.const("update worker set name = 'barney rebel' where name = 'barney'").update
    val fredUpdate = Fragment.const("update worker set name = 'fred flintstone' where name = 'fred'").update
    (barneyUpdate.run *> fredUpdate.run).transact(db).unsafeRunSync()

    val workers = Fragment.const("select * from worker").query[Worker]
    val tasks = Fragment.const("select * from task").query[Task]
    workers.to[List].transact(db).unsafeRunSync.length shouldBe 2
    tasks.to[List].transact(db).unsafeRunSync.length shouldBe 2
  }
}