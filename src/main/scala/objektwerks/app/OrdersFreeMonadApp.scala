package objektwerks.app

object Orders {
  type Symbol = String
  type Amount = Double
  type Response = String
  sealed trait Orders[A]
  case class Buy(stock: Symbol, amount: Amount) extends Orders[Response]
  case class Sell(stock: Symbol, amount: Amount) extends Orders[Response]
}

object OrdersDsl {
  import Orders._
  import cats.free.Free

  type OrdersFree[A] = Free[Orders, A]

  def buy(stock: Symbol, amount: Amount): OrdersFree[Response] = Free.liftF[Orders, Response](Buy(stock, amount))
  def sell(stock: Symbol, amount: Amount): OrdersFree[Response] = Free.liftF[Orders, Response](Sell(stock, amount))
}

object OrdersInterpreter {
  import Orders._
  import cats.{Id, ~>}

  def interpreter: Orders ~> Id = new (Orders ~> Id) {
    def apply[A](order: Orders[A]): Id[A] = order match {
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
  import cats.Id

  private val program = for {
    _ <- buy("APPL", 100.0)
    _ <- buy("MSFT", 10.0)
    response <- sell("GOOG", 110.0)
  } yield response

  def run(): Id[Unit] = program foldMap interpreter
}

/**
  * See: http://perevillega.com/understanding-free-monads
  */
object OrdersFreeMonadApp extends App {
  OrdersProgram.run()
}