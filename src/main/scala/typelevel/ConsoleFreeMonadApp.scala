package typelevel

object Domain {
  sealed trait Console[A]
  case class Prompt(message: String) extends Console[String]
  case class Reply(message: String) extends Console[Unit]

  sealed trait Store[A]
  case class Put(value: String) extends Store[Unit]
  case class List() extends Store[String]
}

object Dsl {
  import cats.free.{Free, Inject}
  import Domain._

  class ConsoleDsl[F[_]](implicit I: Inject[Console, F]) {
    def prompt(message: String): Free[F, String] = Free.inject[Console, F](Prompt(message))
    def reply(message: String): Free[F, Unit] = Free.inject[Console, F](Reply(message))
  }

  class StoreDsl[F[_]](implicit I: Inject[Store, F]) {
    def put(value: String): Free[F, Unit] = Free.inject[Store, F](Put(value))
    def list(): Free[F, String] = Free.inject[Store, F](List())
  }

  implicit def console[F[_]](implicit I: Inject[Console, F]): ConsoleDsl[F] = new ConsoleDsl[F]

  implicit def store[F[_]](implicit I: Inject[Store, F]): StoreDsl[F] = new StoreDsl[F]
}

object Interpreter {
  import scala.io.StdIn._
  import scala.collection.mutable
  import cats.{Id, ~>}
  import Domain._
  private val store = mutable.ListBuffer[String]()

  def consoleInterpreter: Console ~> Id = new (Console ~> Id) {
    def apply[A](c: Console[A]): Id[A] = c match {
      case Prompt(message) => println(message); readLine()
      case Reply(message) => println(message)
    }
  }

  def storeInterpreter: Store ~> Id = new (Store ~> Id) {
    def apply[A](s: Store[A]): Id[A] = s match {
      case Put(value: String) => store.prepend(value)
      case List() => store.mkString(",")
    }
  }
}

object Program {
  import cats.{Id, ~>}
  import cats.data.Coproduct
  import cats.free.Free
  import Domain._
  import Dsl._
  import Interpreter._

  type App[A] = Coproduct[Console, Store, A]
  val interpreter: App ~> Id = consoleInterpreter or storeInterpreter

  def program(implicit C: ConsoleDsl[App], S: StoreDsl[App]): Free[App, Unit] = {
    import C._, S._
    for {
      value <- prompt("Enter value:")
      _ <- put(value)
      values <- list()
      _ <- reply(s"values stored: $values")
    } yield ()
  }

  def run() = {
    program foldMap interpreter
  }
}

/**
  * See: http://www.47deg.com/blog/fp-for-the-average-joe-part3-free-monads
  */
object ConsoleFreeMonadApp extends App {
  Program.run()
}