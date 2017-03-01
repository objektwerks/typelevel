package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class MonadTest extends FunSuite with Matchers {
  test("instances") {
    import cats.Monad
    import cats.instances.option._
    import cats.syntax.option._

    Monad[Option].pure(3) shouldEqual 3.some
    Monad[Option].map(3.some)(n => n * 3) shouldEqual 9.some
    Monad[Option].flatMap(3.some)(n => (n * 3).some) shouldEqual 9.some
  }

  test("syntax") {
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.applicative._
    import cats.syntax.option._

    3.pure[Option] shouldEqual 3.some
    3.pure[List] shouldEqual List(3)
  }
}