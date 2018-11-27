package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class MonadErrorTest extends FunSuite with Matchers {
  test("monad error") {
    import cats.MonadError
    import cats.instances.either._
    import cats.syntax.applicative._
    import cats.syntax.applicativeError._

    type ErrorOr[A] = Either[String, A]
    val monadError = MonadError[ErrorOr, String]

    monadError.pure(3).isRight shouldBe true
    monadError.raiseError("Error").isLeft shouldBe true
    monadError.ensure(monadError.pure(3))("Must equal 3")(_ == 3).isRight shouldBe true
    3.pure[ErrorOr].isRight shouldBe true
    "Error".raiseError[ErrorOr, Int].isLeft shouldBe true
  }
}