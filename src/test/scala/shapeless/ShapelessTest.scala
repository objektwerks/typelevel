package shapeless

import org.scalatest.{FunSuite, Matchers}

case class Value(value: String)

class ShapelessTest extends FunSuite with Matchers {
  test("hlist") {
    val values = 13 :: "labor" :: Value("work") :: HNil
    val i :: s :: cc :: HNil = values
    values.head shouldBe 13
    values.tail shouldBe "labor" :: Value("work") :: HNil
    i shouldBe values.select[Int]
    i shouldBe 13
    s shouldBe values.select[String]
    s shouldBe "labor"
    cc shouldBe values.select[Value]
    cc shouldBe Value("work")
  }
}