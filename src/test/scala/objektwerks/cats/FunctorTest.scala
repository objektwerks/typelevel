package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class FunctorTest extends FunSuite with Matchers {
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