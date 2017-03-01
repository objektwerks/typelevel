package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

// Unable to test === and =!= due to conflict with Scalatest.
class EqTest extends FunSuite with Matchers {
  test("instances") {
    import cats.Eq
    import cats.instances.int._

    val eqInt = Eq[Int]
    eqInt.eqv(1, 1) shouldBe true
    eqInt.neqv(1, 2) shouldBe true
  }
}