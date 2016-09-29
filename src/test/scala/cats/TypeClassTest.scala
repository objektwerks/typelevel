package cats

import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

class TypeClassTest extends FunSuite with Matchers {
  test("semigroup") {
    Semigroup[Int].combine(1, 2) shouldBe 3
  }
}