package objektwerks.doobie

import cats.implicits._
import doobie.imports._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

case class Worker(id: Int = 0, name: String)

case class Task(id: Int = 0, workerId: Int, task: String)

class DoobieTest extends FunSuite with Matchers {
  val db = DriverManagerTransactor[IOLite]( "org.h2.Driver", "jdbc:h2:./target/testdb", "", "" )

  test("ddl > insert > update > select") {
    val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString
    val ddl = Fragment.const(schema).update
    ddl.run.transact(db).unsafePerformIO

    val barney = Fragment.const("insert into worker(name) values ('barney')").update
    val fred = Fragment.const("insert into worker(name) values('fred')").update
    (barney.run *> fred.run).transact(db).unsafePerformIO

    val barneyTask = Fragment.const("insert into task(workerId, task) values(1, 'clean pool')").update
    val fredTask = Fragment.const("insert into task(workerId, task) values(2, 'clean pool')").update
    (barneyTask.run *> fredTask.run).transact(db).unsafePerformIO

    val barneyUpdate = Fragment.const("update worker set name = 'barney rebel' where name = 'barney'").update
    val fredUpdate = Fragment.const("update worker set name = 'fred flintstone' where name = 'fred'").update
    (barneyUpdate.run *> fredUpdate.run).transact(db).unsafePerformIO

    val workers = Fragment.const("select * from worker").query[Worker]
    val tasks = Fragment.const("select * from task").query[Task]
    workers.list.transact(db).unsafePerformIO.length shouldBe 2
    tasks.list.transact(db).unsafePerformIO.length shouldBe 2
  }
}