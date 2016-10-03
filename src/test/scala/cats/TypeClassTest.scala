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
    Monoid[Int].combineAll(List(1, 2)) shouldBe 3
  }
}