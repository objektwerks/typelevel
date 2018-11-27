package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class StateMonadTest extends FunSuite with Matchers {
  test("state monad") {
    import cats.data.State

    val holder = State[Int, String] { state => (state, s"result: $state") }
    val (state, result) = holder.run(3).value
    state shouldEqual 3
    result shouldEqual "result: 3"
    holder.runS(3).value shouldEqual 3
    holder.runA(3).value shouldEqual "result: 3"

    val calc1 = State[Int, String] { state => val calc = state * 2; (calc, s"double: $calc") }
    val calc2 = State[Int, String] { state => val calc = state * 3; (calc, s"triple: $calc") }
    val calc = for {
      d <- calc1
      t <- calc2
    } yield (d, t)
    val (s, r) = calc.run(3).value
    s shouldEqual 18
    r._1 shouldEqual "double: 6"
    r._2 shouldEqual "triple: 18"
  }
}