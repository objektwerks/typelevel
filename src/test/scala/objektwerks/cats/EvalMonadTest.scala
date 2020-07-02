package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class EvalMonadTest extends AnyFunSuite with Matchers {
  test("eval monad") {
    import cats.Eval

    val nowEagerMemoized = Eval.now(math.random)
    val laterLazyMemoized = Eval.later(math.random)
    val alwaysCalculated = Eval.always(math.random)

    nowEagerMemoized.value shouldEqual nowEagerMemoized.value
    laterLazyMemoized.value shouldEqual laterLazyMemoized.value
    alwaysCalculated.value should not equal alwaysCalculated.value

    val task = Eval.always { 3 * 3 }.map { _ * 3 }
    task.value shouldEqual 27

    def factorial(n: BigInt): Eval[BigInt] = if(n == 1) Eval.now(n) else Eval.defer(factorial(n - 1).map(_ * n))
    println(s"stack-safe trampolined factorial: ${factorial(10).value}")
  }
}