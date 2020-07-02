package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MonoidTest extends AnyFunSuite with Matchers {
  test("monoid") {
    import cats.Monoid
    import cats.instances.int._
    import cats.syntax.monoid._

    Monoid[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 shouldEqual 3
    1 |+| 2 |+| Monoid[Int].empty shouldEqual 3
  }
}