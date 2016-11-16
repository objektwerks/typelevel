package typelevel

import org.scalatest.FunSuite

class ValidatedTest extends FunSuite {
  test("instances") {
    import cats.data.Validated
    import cats.data.Validated.Valid
    import cats.data.Validated.Invalid

    assert(Validated.Valid(3) == Valid(3))
    assert(Validated.Invalid("three") == Invalid("three"))
    assert(Validated.valid[String, Int](3) == Valid(3))
    assert(Validated.invalid[String, Int]("three") == Invalid("three"))
  }

  test("syntax") {
    import cats.syntax.validated._
    import cats.data.Validated.Valid
    import cats.data.Validated.Invalid

    assert(3.valid[String] == Valid(3))
    assert("three".invalid[Int] == Invalid("three"))
  }

  test("methods") {
    import cats.data.Validated

    assert(Validated.catchOnly[NumberFormatException]("three".toInt) isInvalid)
    assert(Validated.catchNonFatal(sys.error("Nonfatal")) isInvalid)
    assert(Validated.fromTry(scala.util.Try("three".toInt)) isInvalid)
    assert(Validated.fromEither[String, Int](Left("Error")) isInvalid)
    assert(Validated.fromOption[String, Int](None, "Error") isInvalid)
  }
}