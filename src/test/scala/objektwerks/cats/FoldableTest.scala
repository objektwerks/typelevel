package objektwerks.cats

import org.scalatest.FunSuite

class FoldableTest extends FunSuite {
  test("instances") {
    import cats.Foldable
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.option._

    assert(Foldable[Option].foldLeft(3.some, 3)(_ + _) == 6)
    assert(Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) == 6)
  }
}