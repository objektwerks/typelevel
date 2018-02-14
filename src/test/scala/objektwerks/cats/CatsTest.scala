package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

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
    import cats.instances.option._
    import cats.syntax.monoid._
    import cats.syntax.option._
    import cats.{Monoid, Semigroup}

    Semigroup[Int].combine(1, 2) shouldEqual 3
    Monoid[Int].combine(1, 2) shouldEqual 3
    Monoid[Option[Int]].combine(1.some, 2.some) shouldEqual 3.some

    val x = 1 |+| 2 |+| Monoid[Int].empty
    x shouldEqual 3

    val amount = Amount(1) |+| Amount(2)
    amount shouldEqual Amount(3)
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
    val result = Functor[List].map(list)(_ * 1)
    list shouldEqual result

    val double = (x: Int) => x * x
    val lifted = Functor[Option].lift(double)
    lifted(3.some) shouldEqual 9.some
  }

  test("monad") {
    import cats.Monad
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.applicative._
    import cats.syntax.option._

    Monad[Option].pure(3) shouldEqual 3.some
    Monad[Option].map(3.some)(n => n * 3) shouldEqual 9.some
    Monad[Option].flatMap(3.some)(n => (n * 3).some) shouldEqual 9.some

    3.pure[Option] shouldEqual 3.some
    3.pure[List] shouldEqual List(3)
  }

  test("monad transformer") {
    import cats.data.OptionT
    import cats.instances.list._
    import cats.syntax.applicative._
    import cats.syntax.option._

    type ListOption[A] = OptionT[List, A]
    val result: ListOption[Int] = 3.pure[ListOption]
    result.value shouldEqual List(3.some)
  }
}