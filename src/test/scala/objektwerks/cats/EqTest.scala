package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class EqTest extends FunSuite with Matchers {
  test("eq") {
    import cats.Eq
    import cats.instances.int._
    import cats.syntax.eq._

    val eqInt = Eq[Int]
    eqInt.eqv(1, 1) shouldBe true
    eqInt.neqv(1, 2) shouldBe true
    assert(1 =!= 3)
  }
}