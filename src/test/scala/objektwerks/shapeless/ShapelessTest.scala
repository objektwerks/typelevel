package objektwerks.shapeless

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import shapeless.HNil

class ShapelessTest extends AnyFunSuite with Matchers {
  test("hlist") {
    val hlist = List(3) :: List("3") :: HNil
    hlist.head shouldBe List(3)
    hlist.tail shouldBe List("3") :: HNil
  }
}