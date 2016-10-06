import cats.free._
import cats.{Id, ~>}

import scala.io.StdIn._

sealed trait Interact[A]
case class Ask(prompt: String) extends Interact[String]
case class Tell(reply: String) extends Interact[Unit]

object InteractionOps {

  def ask(prompt: String) : Free[Interact, String] = Free.liftF[Interact, String](Ask(prompt))
  def tell(reply: String) : Free[Interact, Unit] = Free.liftF[Interact, Unit](Tell(reply))
}

object InteractInterpreter {
  def interpreter: Interact ~> Id = new (Interact ~> Id) {
    def apply[A](fa: Interact[A]): Id[A] = fa match {
      case Ask(prompt) => println(prompt); readLine()
      case Tell(reply) => println(reply)
    }
  }
}

object InteractProgram {
  import InteractionOps._
  import InteractInterpreter._

  val program = for {
    person <- ask("What's your name?")
    _ <- tell(s"Nice to meet you, $person")
  } yield ()

  def run(): Id[Unit] = program foldMap interpreter
}

/**
  * Intellij complains of 4 errors, but sbt run works!
  * See: http://www.47deg.com/blog/fp-for-the-average-joe-part3-free-monads
  * See: http://perevillega.com/understanding-free-monads
  */
object FreeMonadApp extends App {
  InteractProgram.run()
}