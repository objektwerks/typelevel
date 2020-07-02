package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ShowTest extends AnyFunSuite with Matchers {
  test("show") {
    import cats.Show
    import cats.instances.int._
    import cats.syntax.show._

    Show[Int].show(1) shouldEqual "1"
    1.show shouldEqual "1"
  }
}