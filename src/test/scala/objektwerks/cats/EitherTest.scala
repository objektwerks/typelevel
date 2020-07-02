package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class EitherTest extends AnyFunSuite with Matchers {
  test("either") {
    import cats.syntax.either._
    import scala.util.Try

    val z = for {
      x <- 1.asRight
      y <- 2.asRight
    } yield x + y
    z.getOrElse(0) shouldEqual 3

    Either.catchOnly[NumberFormatException]("three".toInt).isLeft shouldBe true
    Either.catchNonFatal(sys.error("Nonfatal")).isLeft shouldBe true
    Either.fromTry(Try("three".toInt)).isLeft shouldBe true

    3.asRight.ensure("Must be equal to 3.")(_ == 3).isRight shouldBe true
  }
}