package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

case class Amount(value: Double)
object Amount {
  import cats.Monoid
  implicit val amountMonoid: Monoid[Amount] = new Monoid[Amount] {
    def empty: Amount = Amount(0)
    def combine(x: Amount, y: Amount): Amount = Amount(x.value + y.value)
  }
}

class CatsTest extends FunSuite with Matchers {
  test("eq") {
    import cats.Eq
    import cats.instances.int._

    val eqInt = Eq[Int]
    eqInt.eqv(1, 1) shouldBe true
    eqInt.neqv(1, 2) shouldBe true
  }

  test("show") {
    import cats.Show
    import cats.instances.int._
    import cats.syntax.show._

    val showInt = Show[Int]
    showInt.show(1) shouldEqual "1"
    1.show shouldEqual "1"
  }

  test("monoid") {
    import cats.instances.int._
    import cats.syntax.monoid._
    import cats.{Monoid, Semigroup}

    Semigroup[Int].combine(1, 2) shouldEqual 3
    Monoid[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 |+| Monoid[Int].empty shouldEqual 3
    Amount(1) |+| Amount(2) shouldEqual Amount(3)
  }

  test("foldable") {
    import cats.Foldable
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.option._

    Foldable[Option].foldLeft(3.some, 3)(_ + _) shouldEqual 6
    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldEqual 6
  }

  test("functor") {
    import cats.Functor
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.option._

    val list = List(1, 2)
    Functor[List].map(list)(_ * 3) shouldEqual List(3, 6)

    val double = (x: Int) => x * x
    val lifted = Functor[Option].lift(double)
    lifted(3.some) shouldEqual 9.some
  }

  test("monad") {
    import cats.Monad
    import cats.instances.option._
    import cats.syntax.option._

    Monad[Option].pure(3) shouldEqual 3.some
    Monad[Option].map(3.some)(n => n * 3) shouldEqual 9.some
    Monad[Option].flatMap(3.some)(n => (n * 3).some) shouldEqual 9.some
  }
}