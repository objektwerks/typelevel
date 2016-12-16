package objektwerks.cats

import org.scalatest.FunSuite

class MonadTransformerTest extends FunSuite {
  test("transform") {
    import cats.data.OptionT
    import cats.instances.list._
    import cats.syntax.applicative._
    import cats.syntax.option._

    type ListOption[A] = OptionT[List, A]
    val result: ListOption[Int] = 3.pure[ListOption]
    assert(result.value == List(3.some))
  }
}