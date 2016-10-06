import cats.free._
import cats.{Id, ~>}

import scala.io.StdIn._

object Interact {
  sealed trait Interact[A]
  case class Ask(message: String) extends Interact[String]
  case class Tell(message: String) extends Interact[Unit]
}

object InteractDsl {
  import Interact._

  def ask(message: String) : Free[Interact, String] = Free.liftF[Interact, String](Ask(message))
  def tell(message: String) : Free[Interact, Unit] = Free.liftF[Interact, Unit](Tell(message))
}

object InteractInterpreter {
  import Interact._

  def interpreter: Interact ~> Id = new (Interact ~> Id) {
    def apply[A](fa: Interact[A]): Id[A] = fa match {
      case Ask(message) => println(message); readLine()
      case Tell(message) => println(message)
    }
  }
}

object InteractProgram {
  import InteractDsl._
  import InteractInterpreter._

  val program = for {
    person <- ask("What's your name?")
    _ <- tell(s"Nice to meet you, $person!")
  } yield ()

  def run(): Id[Unit] = program foldMap interpreter
}

/**
  * Intellij complains of 4 errors, but sbt run works!
  * See: http://www.47deg.com/blog/fp-for-the-average-joe-part3-free-monads
  */
object InteractFreeMonadApp extends App {
  InteractProgram.run()
}