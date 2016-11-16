package typelevel

import org.scalatest.FunSuite

class MonadTest extends FunSuite {
  test("instances") {
    import cats.Monad
    import cats.instances.option._
    import cats.syntax.option._

    assert(Monad[Option].pure(3) == 3.some)
    assert(Monad[Option].map(3.some)(n => n * 3) == 9.some)
    assert(Monad[Option].flatMap(3.some)(n => (n * 3).some) == 9.some)
  }

  test("syntax") {
    import cats.instances.option._
    import cats.syntax.applicative._
    import cats.syntax.option._
    import cats.instances.list._

    assert(3.pure[Option] == 3.some)
    assert(3.pure[List] == List(3))
  }
}