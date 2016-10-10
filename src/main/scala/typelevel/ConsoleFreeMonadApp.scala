package typelevel

import cats.free.Free
import cats._

import scala.io.StdIn._

object Console {
  sealed trait Console[A]
  case class Prompt(message: String) extends Console[String]
  case class Reply(message: String) extends Console[Unit]
}

object ConsoleDsl {
  import Console._

  def prompt(message: String) : Free[Console, String] = Free.liftF[Console, String](Prompt(message))
  def reply(message: String) : Free[Console, Unit] = Free.liftF[Console, Unit](Reply(message))
}

object ConsoleInterpreter {
  import Console._

  def interpreter: Console ~> Id = new (Console ~> Id) {
    def apply[A](fa: Console[A]): Id[A] = fa match {
      case Prompt(message) => println(message); readLine()
      case Reply(message) => println(message)
    }
  }
}

object ConsoleProgram {
  import ConsoleDsl._
  import ConsoleInterpreter._

  val program = for {
    person <- prompt("What's your name?")
    _ <- reply(s"Nice to meet you, $person!")
  } yield ()

  def run(): Id[Unit] = program foldMap interpreter
}

/**
  * See: http://www.47deg.com/blog/fp-for-the-average-joe-part3-free-monads
  */
object ConsoleFreeMonadApp extends App {
  ConsoleProgram.run()
}