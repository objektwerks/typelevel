package shapeless

import org.scalatest.FunSuite

case class Value(value: String)

class ShapelessTest extends FunSuite {
  test("hlist") {
    val values = 13 :: "labor" :: Value("work") :: HNil
    val i :: s :: cc :: HNil = values
    assert(values.head == 13)
    assert(values.tail == "labor" :: Value("work") :: HNil)
    assert(i == values.select[Int] && i == 13)
    assert(s == values.select[String] && s == "labor")
    assert(cc == values.select[Value] && cc == Value("work"))
  }
}