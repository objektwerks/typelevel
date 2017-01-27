package objektwerks.doobie

import cats.implicits._
import doobie.imports._
import fs2.interop.cats._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class DoobieTest extends FunSuite with Matchers {
  val db = DriverManagerTransactor[IOLite]( "org.h2.Driver", "jdbc:h2:mem:test", "", "" )

  test("embedded ddl") {
    val drop: Update0 =
      sql"""
           drop table if exists worker;
           drop table if exists task;
        """.update
    val create: Update0 =
      sql"""
           create table worker (id int primary key not null, name varchar(64) not null);
           create table task (id int primary key not null, workerId int not null, task varchar(128) not null, foreign key(workerId) references worker (id));
        """.update
    (drop.run *> create.run).transact(db).unsafePerformIO
  }

  test("file ddl") {
    val schema = Source.fromInputStream(getClass.getResourceAsStream("/schema.sql")).mkString
    val ddl: Update0 = sql"$schema".update
    ddl.run.transact(db).unsafePerformIO
  }
}