package shapeless

import org.scalatest.FunSuite

case class Value(value: String)

class ShapelessTet extends FunSuite {
  test("hlist") {
    val values = 13 :: "labor" :: Value("work") :: HNil
    val n :: s :: c :: HNil = values
    assert(values.head == 13)
    assert(values.tail == "labor" :: Value("work") :: HNil)
    assert(n == values.select[Int] && n == 13)
    assert(s == values.select[String] && s == "labor")
    assert(c == values.select[Value] && c == Value("work"))
  }
}