package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class MonoidTest extends FunSuite with Matchers {
  test("monoid") {
    import cats.Monoid
    import cats.instances.int._
    import cats.syntax.monoid._

    Monoid[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 shouldEqual 3
    1 |+| 2 |+| Monoid[Int].empty shouldEqual 3
  }
}