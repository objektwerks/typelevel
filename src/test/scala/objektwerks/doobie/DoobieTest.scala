package objektwerks.doobie

import doobie.imports._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class DoobieTest extends FunSuite with Matchers {
  val db = DriverManagerTransactor[IOLite]( "org.h2.Driver", "jdbc:h2:mem:test", "", "" )

  test("ddl") {
    val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString
    val ddl: Update0 = Fragment.const(schema).update
    ddl.run.transact(db).unsafePerformIO
  }
}