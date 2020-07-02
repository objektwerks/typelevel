package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class FunctorTest extends AnyFunSuite with Matchers {
  test("functor") {
    import cats.Functor
    import cats.instances.list._
    import cats.instances.option._

    Functor[List].map(List(1, 2))(_ * 3) shouldEqual List(3, 6)
    Functor[Option].map(Option(3))(_ * 3) shouldEqual Some(9)

    val incr = (i: Int) => i + 1
    val liftedIncr = Functor[Option].lift(incr)
    liftedIncr(Option(1)) shouldEqual Some(2)
  }
}