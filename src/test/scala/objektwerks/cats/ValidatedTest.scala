package objektwerks.cats

import cats.data.Validated.{Invalid, Valid, _}
import cats.data.ValidatedNec
import cats.implicits._
import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

case class Profile(user: String, password: String)

object ProfileValidator {
  private val userRegex = "^[a-zA-Z0-9]+$".r
  private val userError = "Special characters forbidden."
  private val passwordRegex = "(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$".r
  private val passwordError = "10+ chars, 1 uppercase char, 1 lowercase char, 1 number, 1 special char."

  sealed trait ProfileInvalid { def error: String }
  case object UserInvalid extends ProfileInvalid { def error: String = userError }
  case object PasswordInvalid extends ProfileInvalid { def error: String = passwordError }

  type ValidationResult[A] = ValidatedNec[ProfileInvalid, A]

  def validateProfile(user: String, password: String): ValidationResult[Profile] =
    (validateUser(user), validatePassword(password)).mapN(Profile)

  private def validateUser(user: String): ValidationResult[String] =
    if (userRegex.matches(user)) user.validNec
    else UserInvalid.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (passwordRegex.matches(password)) password.validNec
    else PasswordInvalid.invalidNec
}

class ValidatedTest extends FunSuite with Matchers {
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
    import objektwerks.cats.ProfileValidator._

    val profile = Profile("typelevel", "@Typelevel_Validated_2.1.0")
    val validatedProfile = validateProfile(profile.user, profile.password)
    validatedProfile.isValid shouldBe true
    validatedProfile.toEither match {
      case Right(value) => value shouldEqual profile; println(value)
      case Left(_) => fail()
    }
  }

  test("validator invalid") {
    import objektwerks.cats.ProfileValidator._

    val profile = Profile("", "")
    val validatedProfile = validateProfile(profile.user, profile.password)
    validatedProfile.isInvalid shouldBe true
    validatedProfile.toEither.left.toSeq.head.length shouldBe 2
    validatedProfile.toEither match {
      case Right(_) => fail()
      case Left(invalids) => invalids.map {
        case ui @ UserInvalid => ui.error shouldEqual UserInvalid.error; println(ui.error)
        case pi @ PasswordInvalid => pi.error shouldEqual PasswordInvalid.error; println(pi.error)
      }
    }
  }
}