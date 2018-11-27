package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

class ValidatedTest extends FunSuite with Matchers {
  test("validated") {
    import cats.data.Validated._
    import cats.data.Validated.{Invalid, Valid}
    import cats.syntax.validated._

    valid[String, Int](3) shouldBe Valid(3)
    invalid[String, Int]("three") shouldBe Invalid("three")
    catchOnly[NumberFormatException]("three".toInt).isInvalid shouldBe true
    catchNonFatal(sys.error("Nonfatal")).isInvalid shouldBe true
    fromTry(Try("three".toInt)).isInvalid shouldBe true
    fromEither[String, Int](Left("Error")).isInvalid shouldBe true
    fromOption[String, Int](None, "Error").isInvalid shouldBe true

    3.valid.map(_ * 3) shouldBe Valid(9)
    3.valid[String] shouldBe Valid(3)
    "three".invalid[Int] shouldBe Invalid("three")
  }
}