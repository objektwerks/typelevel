package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class MonadTransformerTest extends FunSuite with Matchers {
  test("transform") {
    import cats.data.OptionT
    import cats.instances.list._
    import cats.syntax.applicative._
    import cats.syntax.option._

    type ListOption[A] = OptionT[List, A]
    val result: ListOption[Int] = 3.pure[ListOption]
    result.value shouldEqual List(3.some)
  }
}