package objektwerks.validated

import org.scalatest.{FunSuite, Matchers}

class ValidatedTest extends FunSuite with Matchers {
  test("instances") {
    import cats.data.Validated
    import cats.data.Validated.{Invalid, Valid}
    import cats.syntax.validated._

    Validated.Valid(3) shouldEqual Valid(3)
    Validated.Invalid("three") shouldEqual Invalid("three")
    Validated.valid[String, Int](3) shouldEqual Valid(3)
    Validated.invalid[String, Int]("three") shouldEqual Invalid("three")

    3.valid[String] shouldEqual Valid(3)
    "three".invalid[Int] shouldEqual Invalid("three")

    Validated.catchOnly[NumberFormatException]("three".toInt).isInvalid shouldBe true
    Validated.catchNonFatal(sys.error("Nonfatal")).isInvalid shouldBe true
    Validated.fromTry(scala.util.Try("three".toInt)).isInvalid shouldBe true
    Validated.fromEither[String, Int](Left("Error")).isInvalid shouldBe true
    Validated.fromOption[String, Int](None, "Error").isInvalid shouldBe true
    3.valid.map(_ * 3) shouldEqual Valid(9)
  }
}