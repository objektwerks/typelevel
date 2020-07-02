package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class FoldableTest extends AnyFunSuite with Matchers {
  test("foldable") {
    import cats.Foldable
    import cats.instances.int._
    import cats.instances.list._
    import cats.instances.string._
    import cats.syntax.foldable._

    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldEqual 6
    Foldable[List].combineAll(List(1, 2, 3)) shouldEqual 6
    Foldable[List].foldMap(List(1, 2, 3))(_.toString) shouldEqual "123"
    (Foldable[List] compose Foldable[List]).combineAll(List(List(1, 2, 3), List(4, 5, 6))) shouldEqual 21

    List(1, 2, 3).combineAll shouldEqual 6
    List(1, 2, 3).foldMap(_.toString) shouldEqual "123"
  }
}