package shapeless

import org.scalatest.{FunSuite, Matchers}

case class Value(value: String)

class ShapelessTest extends FunSuite with Matchers {
  test("hlist") {
    val values = 13 :: "labor" :: Value("work") :: HNil
    val i :: s :: c :: HNil = values
    values.head shouldBe 13
    values.tail shouldBe "labor" :: Value("work") :: HNil
    i shouldBe 13
    i shouldBe values.select[Int]
    s shouldBe "labor"
    s shouldBe values.select[String]
    c shouldBe Value("work")
    c shouldBe values.select[Value]
  }
}