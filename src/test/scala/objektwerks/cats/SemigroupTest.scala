package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SemigroupTest extends AnyFunSuite with Matchers {
  test("semigroup") {
    import cats.Semigroup
    import cats.instances.int._
    import cats.syntax.semigroup._

    Semigroup[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 shouldEqual 3
  }
}