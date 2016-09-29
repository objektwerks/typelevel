package cat

import cats._
import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

class TypeClassesTest extends FunSuite with Matchers {
  test("semigroup") {
    Semigroup[Int].combine(1, 2)  shouldBe 3
  }
}