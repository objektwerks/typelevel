package objektwerks.cats

import cats.data.Validated.{Invalid, Valid, _}
import cats.data.ValidatedNec
import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

case class Profile(user: String, password: String)

sealed trait ProfileValidator {
  sealed trait ProfileInvalid {
    def error: String
  }

  case object UserInvalid extends ProfileInvalid {
    def error: String = "Special characters not allowed."
  }

  case object PasswordInvalid extends ProfileInvalid {
    def error: String = "At least 10 characters, 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character."
  }

  type ValidationResult[A] = ValidatedNec[ProfileInvalid, A]

  private def validateUser(user: String): ValidationResult[String] =
    if (user.matches("^[a-zA-Z0-9]+$")) user.validNec else UserInvalid.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else PasswordInvalid.invalidNec

  def validateProfile(user: String, password: String): ValidationResult[Profile] = {
    (validateUser(user), validatePassword(password)).mapN(Profile)
  }
}

class ValidatedTest extends FunSuite with Matchers with ProfileValidator {
  test("valid") {
    valid[String, Int](3) shouldBe Valid(3)
    3.valid.map(_ * 3) shouldBe Valid(9)
  }

  test("invalid") {
    invalid[String, Int]("three") shouldBe Invalid("three")
    "three".invalid[Int] shouldBe Invalid("three")
  }

  test("catch") {
    catchOnly[NumberFormatException]("three".toInt).isInvalid shouldBe true
    catchNonFatal(sys.error("Nonfatal")).isInvalid shouldBe true
  }

  test("from") {
    fromTry(Try("three".toInt)).isInvalid shouldBe true
    fromEither[String, Int](Left("Error")).isInvalid shouldBe true
    fromOption[String, Int](None, "Error").isInvalid shouldBe true
  }

  test("validator valid") {
    val profile = Profile("typelevel", "@Typelevel_Validated_2.1.0")
    val validatedProfile = validateProfile(profile.user, profile.password)
    validatedProfile.isValid shouldBe true
    validatedProfile.toEither match {
      case Right(value) => value shouldEqual profile
      case Left(_) => fail()
    }
  }

  test("validator invalid") {
    val profile = Profile("", "")
    val validatedProfile = validateProfile(profile.user, profile.password)
    validatedProfile.isInvalid shouldBe true
    validatedProfile.toEither match {
      case Right(_) => fail()
      case Left(invalids) => invalids.map(invalid => invalid shouldBe a[ProfileInvalid])
    }
  }
}