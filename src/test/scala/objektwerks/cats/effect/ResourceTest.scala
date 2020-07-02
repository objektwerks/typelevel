package objektwerks.cats.effect

import cats.effect.{IO, Resource}

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class ResourceTest extends AnyFunSuite with Matchers {
  test("auto closeable") {
    val file = IO { Source.fromFile("build.sbt") }
    Resource
      .fromAutoCloseable(file)
      .use(source => IO( source.mkString.nonEmpty shouldBe true) )
      .unsafeRunSync()
  }

  test("bracket") {
    IO( Source.fromFile("build.sbt") ).bracket {
      file => IO( file.mkString.nonEmpty shouldBe true )
    } {
      file => IO( file.close() )
    }
  }
}