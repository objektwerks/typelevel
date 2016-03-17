package shapeless

import org.scalatest.{FunSuite, Matchers}

case class Value(value: String)

object plusOneOps extends Poly1 {
  implicit def caseInt = at[Int]{ _ + 1 }
  implicit def caseString = at[String]{ _ + 1 }
  implicit def caseValue = at[Value] { case Value(value) => Value(value + 1) }
}

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

    values.map(plusOneOps) shouldBe 14 :: "labor1" :: Value("work1") :: HNil

    val generic = Generic[Value]
    val hlist = generic.to(Value("some"))
    hlist shouldBe "some" :: HNil
    generic.from(hlist) shouldBe Value("some")

    import shapeless.syntax.std.tuple._
    val tuple = (3, 4, 5)
    3 shouldBe tuple.head
    (4, 5) shouldBe tuple.tail
  }
}