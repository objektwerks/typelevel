package cats

import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

class TypeClassTest extends FunSuite with Matchers {
  test("semigroup") {
    Semigroup[Int].combine(1, 2) shouldBe 3
    Option(1) |+| Option(2) shouldBe Some(3)
  }

  test("monoid") {
    Monoid[Int].combine(1, 2) shouldBe 3
    Monoid[Int].combineAll( List(1, 2) ) shouldBe 3
  }

  test("functor") {
    Functor[List].map( List(1, 2) ) (_ + 1) shouldBe List(2, 3)
    Functor[List].fproduct( List(1, 2) ) (_ + 1) shouldBe List( (1, 2), (2, 3))
    Functor[List].fproduct( List(1, 2) ) (_ + 1).toMap shouldBe Map( 1 -> 2, 2 -> 3 )
  }

  test("applicative") {

  }

  test("monad") {

  }
}