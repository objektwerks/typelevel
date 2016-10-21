package typelevel

import cats._
import cats.implicits._
import cats.syntax.eq._

import org.scalatest.{FunSuite, Matchers}

class CatsTest extends FunSuite with Matchers {
  test("eq") {
    1 === 1 shouldBe true
    1 === "a" shouldBe false
  }
  
  test("semigroup") {
    Semigroup[Int].combine(1, 2) shouldBe 3
    1.some |+| 2.some shouldBe 3.some
  }

  test("monoid") {
    Monoid[Int].combine(1, 2) shouldBe 3
    Monoid[Int].combineAll( List(1, 2) ) shouldBe 3
  }

  test("functor") {
    Functor[Id].map( 2 )(_ + 1) shouldBe 3
    Functor[List].map( List(1, 2) )(_ + 1) shouldBe List(2, 3)
    Functor[List].fproduct( List(1, 2) )(_ + 1) shouldBe List( (1, 2), (2, 3) )
    Functor[List].fproduct( List(1, 2) )(_ + 1).toMap shouldBe Map( 1 -> 2, 2 -> 3 )
  }

  test("apply") {
    Apply[Id].ap( { _ + 1 }: Id[Int => Int] ) ( 2 ) shouldBe 3
    val incr: Int => Int = _ + 1
    Apply[Option].ap(Some(incr)) (Some(1)) shouldBe 2.some
    val multiply: (Int, Int) => Int = _ * _
    val multipliers = 3.some |@| 3.some
    multipliers map multiply shouldBe 9.some
  }

  test("applicative") {
    Applicative[Id].pure(33) shouldBe 33
    Applicative[Option].pure(1) shouldBe 1.some
    Applicative[List].pure(1) shouldBe List(1)
  }

  test("flatmap") {
    FlatMap[Id].flatMap(2) { _ + 1 } shouldBe 3
    2.some flatMap { (x: Int) => (x + 1).some } shouldBe 3.some
  }

  test("monad") {
    Monad[Id].map( 2 )(_ + 1) shouldBe 3
    Monad[Id].flatMap( 2 )(_ + 1) shouldBe 3
    Monad[Option].flatMap( Option(Option(3)) )( x => x ) shouldBe 3.some
    Monad[List].flatMap( List(1, 2) )( x => List(x + 1) ) shouldBe List(2, 3)
  }

  test("foldable") {
    Foldable[List].foldLeft( List(1, 2), 0 )(_ + _ ) shouldBe 3
    Foldable[List].foldRight( List(1, 2), Now(0) )( (x, rest) ⇒ Later(x + rest.value) ).value shouldBe 3
    Foldable[List].fold( List(1, 2) ) shouldBe 3
    Foldable[List].foldMap(List("a", "b", "c"))(_.length) shouldBe 3
    Foldable[List].foldK( List(List(1, 2), List(3, 4) )) shouldBe List(1, 2, 3, 4)
  }
}