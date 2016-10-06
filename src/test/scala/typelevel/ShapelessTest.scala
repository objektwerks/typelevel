package typelevel

import org.scalatest.{FunSuite, Matchers}
import shapeless.HNil

class ShapelessTest extends FunSuite with Matchers {
  test("hlist") {
    val hlist = 33 :: "test" :: HNil
    hlist.head shouldBe 33
    hlist.tail shouldBe "test" :: HNil
    hlist.select[Int] shouldBe 33
    hlist.select[String] shouldBe "test"
  }
}