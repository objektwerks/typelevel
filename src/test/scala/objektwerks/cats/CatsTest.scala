package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

import scala.util.Try

class CatsTest extends FunSuite with Matchers {
  test("eq") {
    import cats.Eq
    import cats.instances.int._
    import cats.syntax.eq._

    val eqInt = Eq[Int]
    eqInt.eqv(1, 1) shouldBe true
    eqInt.neqv(1, 2) shouldBe true
    // Conflict with Scalatest? assert(1 === 1)
    assert(1 =!= 3)
  }

  test("show") {
    import cats.Show
    import cats.instances.int._
    import cats.syntax.show._

    Show[Int].show(1) shouldEqual "1"
    1.show shouldEqual "1"
  }

  test("semigroup") {
    import cats.Semigroup
    import cats.instances.int._
    import cats.syntax.semigroup._

    Semigroup[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 shouldEqual 3
  }

  test("monoid") {
    import cats.Monoid
    import cats.instances.int._
    import cats.syntax.monoid._

    Monoid[Int].combine(1, 2) shouldEqual 3
    1 |+| 2 shouldEqual 3
    1 |+| 2 |+| Monoid[Int].empty shouldEqual 3
  }

  test("foldable") {
    import cats.Foldable
    import cats.instances.int._
    import cats.instances.list._
    import cats.instances.string._
    import cats.syntax.foldable._

    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldEqual 6
    Foldable[List].combineAll(List(1, 2, 3)) shouldEqual 6
    Foldable[List].foldMap(List(1, 2, 3))(_.toString) shouldEqual "123"
    (Foldable[List] compose Foldable[List]).combineAll(List(List(1, 2, 3), List(4, 5, 6))) shouldEqual 21

    List(1, 2, 3).combineAll shouldEqual 6
    List(1, 2, 3).foldMap(_.toString) shouldEqual "123"
  }

  test("traverse") {
    import cats.Traverse
    import cats.instances.future._
    import cats.instances.list._
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    val listOfFutures = List(Future(1), Future(2), Future(3))
    val sequence = Traverse[List].sequence(listOfFutures)
    sequence foreach { xs => assert(xs == List(1, 2, 3)) }

    val incr = (i: Int) => Future(i + 1)
    val list = List(1, 2, 3)
    val traversal = Traverse[List].traverse(list)(incr)
    traversal foreach { xs => assert( xs == List(2, 3, 4)) }
  }

  test("functor") {
    import cats.Functor
    import cats.instances.list._
    import cats.instances.option._

    Functor[List].map(List(1, 2))(_ * 3) shouldEqual List(3, 6)
    Functor[Option].map(Option(3))(_ * 3) shouldEqual Some(9)

    val incr = (i: Int) => i + 1
    val liftedIncr = Functor[Option].lift(incr)
    liftedIncr(Option(1)) shouldEqual Some(2)
  }

  test("monad") {
    import cats.Monad
    import cats.instances.option._
    import cats.instances.list._
    import cats.syntax.option._
    import cats.syntax.applicative._
    import cats.syntax.functor._
    import cats.syntax.flatMap._
    import cats.Id

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

  test("either") {
    import cats.syntax.either._

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

  test("validated") {
    import cats.data.Validated._
    import cats.data.Validated.{Invalid, Valid}
    import cats.syntax.validated._

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
}