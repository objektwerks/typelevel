package objektwerks.cats

import org.scalatest.FunSuite

// Can't test === and =!= due to Scalatest conflict.
class EqTest extends FunSuite {
  test("instances") {
    import cats.Eq
    import cats.instances.int._

    val eqInt = Eq[Int]
    assert(eqInt.eqv(1, 1))
    assert(eqInt.neqv(1, 2))
  }
}