package objektwerks.cats

import cats.data.Validated.{Invalid, Valid, _}
import cats.data.ValidatedNec
import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

case class Profile(user: String, password: String)

sealed trait ProfileValidator {
  sealed trait ProfileValidation {
    def error: String
  }
  case object UserHasSpecialCharacters extends ProfileValidation {
    def error: String = "Special characters not allowed."
  }
  case object PasswordDoesNotMeetCriteria extends ProfileValidation {
    def error: String = "At least 10 characters, one uppercase letter, one lowercase letter, one number and one special character."
  }
  type ValidationResult[A] = ValidatedNec[ProfileValidation, A]

  private def validateUser(user: String): ValidationResult[String] =
    if (user.matches("^[a-zA-Z0-9]+$")) user.validNec else UserHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec

  def validateProfile(user: String, password: String): ValidationResult[Profile] = {
    (validateUser(user), validatePassword(password)).mapN(Profile)
  }
}


class ValidatedTest extends FunSuite with Matchers with ProfileValidator {
  test("valid > invalid") {
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

  test("validator") {
    val validProfile = Profile("typelevel", "@Typelevel_Validated_2.1.0")
    validateProfile(validProfile.user, validProfile.password).isValid shouldBe true

    val invalidProfile = Profile("typelevel", "typelevel")
    validateProfile(invalidProfile.user, invalidProfile.password).isValid shouldBe false
  }
}