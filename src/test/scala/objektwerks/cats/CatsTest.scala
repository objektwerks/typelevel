package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class CatsTest extends FunSuite with Matchers {
  test("eq") {
    import cats.Eq
    import cats.instances.int._
    import cats.syntax.eq._

    val eqInt = Eq[Int]
    eqInt.eqv(1, 1) shouldBe true
    eqInt.neqv(1, 2) shouldBe true
    // Conflict with Scalatest? assert(1 === 1)
    assert(1 =!= 3)
  }

  test("show") {
    import cats.Show
    import cats.instances.int._
    import cats.syntax.show._

    val showInt = Show[Int]
    showInt.show(1) shouldEqual "1"
    1.show shouldEqual "1"
  }

  test("semigroup") {
    import cats.Semigroup
    import cats.instances.int._
    import cats.syntax.semigroup._

    Semigroup[Int].combine(1, 2) shouldEqual 3
    1.combine(2) shouldEqual 3
  }

  test("monoid") {
    import cats.Monoid
    import cats.instances.int._
    import cats.syntax.monoid._

    Monoid[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 |+| Monoid[Int].empty shouldEqual 3
  }

  test("foldable") {
    import cats.Foldable
    import cats.instances.list._

    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldEqual 6
  }

  test("functor") {
    import cats.Functor
    import cats.instances.list._

    Functor[List].map(List(1, 2))(_ * 3) shouldEqual List(3, 6)
  }

  test("monad") {
    import cats.Monad
    import cats.instances.option._
    import cats.syntax.option._

    Monad[Option].flatMap(3.some)(i => (i * 3).some).sum shouldEqual 9
    Monad[Option].map(3.some)(_ * 3) shouldEqual 9.some
  }
}