package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class FoldableTest extends FunSuite with Matchers {
  test("instances") {
    import cats.Foldable
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.option._

    Foldable[Option].foldLeft(3.some, 3)(_ + _) shouldEqual 6
    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldEqual 6
  }
}