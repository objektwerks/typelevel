package objektwerks.shapeless

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import shapeless.HNil

class ShapelessTest extends AnyFunSuite with Matchers {
  test("hlist") {
    val hlist = 3 :: "3" :: HNil
    hlist.head shouldBe 3
    hlist.tail shouldBe "3" :: HNil
  }
}