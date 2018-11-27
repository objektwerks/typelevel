package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class MonadTest extends FunSuite with Matchers {
  test("monad") {
    import cats.Monad
    import cats.instances.option._
    import cats.instances.list._
    import cats.syntax.option._
    import cats.syntax.applicative._
    import cats.syntax.functor._
    import cats.syntax.flatMap._
    import cats.Id
    import scala.util.Try

    val square = (i: Int) => i * i
    val cube = (i: Int) => Some(i * i * i)
    Monad[Option]pure 3 shouldEqual 3.some
    Monad[Option].map(3.some)(square).getOrElse(0) shouldEqual 9
    Monad[Option].flatMap(3.some)(cube).getOrElse(0) shouldEqual 27

    val toInt = (s: String) => Try(s.toInt).toOption
    val list = List("1", "2", "3", "four")
    3.pure[List] shouldEqual List(3)
    Monad[List].map(list)(toInt).flatten.sum shouldEqual 6
    Monad[List].flatMap(list)(s => toInt(s).toList).sum shouldEqual 6

    def sum[F[_]: Monad](x: F[Int], y: F[Int]): F[Int] = x.flatMap(a => y.map(b => a + b))
    sum(3.some, 3.some).getOrElse(0) shouldEqual 6
    sum(List(3), List(3)).sum shouldEqual 6
    sum(3 : Id[Int], 3 : Id[Int]) shouldEqual 6
  }
}