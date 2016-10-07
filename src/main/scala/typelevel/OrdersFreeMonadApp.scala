package typelevel

import cats.free.Free
import cats._

object Orders {
  sealed trait Orders[A]
  case class Buy(stock: String, amount: Int) extends Orders[String]
  case class Sell(stock: String, amount: Int) extends Orders[String]
}

object OrdersDsl {
  import Orders._

  type OrdersFree[A] = Free[Orders, A]

  def buy(stock: String, amount: Int): OrdersFree[String] = Free.liftF[Orders, String](Buy(stock, amount))
  def sell(stock: String, amount: Int): OrdersFree[String] = Free.liftF[Orders, String](Sell(stock, amount))
}

object OrdersInterpreter {
  import Orders._

  def interpreter: Orders ~> Id = new (Orders ~> Id) {
    def apply[A](fa: Orders[A]): Id[A] = fa match {
      case Buy(stock, amount) =>
        println(s"Buying $amount of $stock")
        "ok"
      case Sell(stock, amount) =>
        println(s"Selling $amount of $stock")
        "ok"
    }
  }
}

object OrdersProgram {
  import OrdersDsl._
  import OrdersInterpreter._

  val program = for {
    _ <- buy("APPL", 100)
    _ <- buy("MSFT", 10)
    response <- sell("GOOG", 110)
  } yield response

  def run(): Id[Unit] = program foldMap interpreter
}

/**
  * Intellij complains of 4 errors, but sbt run works!
  * See: http://perevillega.com/understanding-free-monads
  */
object OrdersFreeMonadApp extends App {
  OrdersProgram.run()
}