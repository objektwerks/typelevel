package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TraverseTest extends AnyFunSuite with Matchers {
  test("traverse") {
    import cats.Traverse
    import cats.instances.future._
    import cats.instances.list._
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    val listOfFutures = List(Future(1), Future(2), Future(3))
    val sequence = Traverse[List].sequence(listOfFutures)
    sequence foreach { xs => assert(xs == List(1, 2, 3)) }

    val incr = (i: Int) => Future(i + 1)
    val list = List(1, 2, 3)
    val traversal = Traverse[List].traverse(list)(incr)
    traversal foreach { xs => assert( xs == List(2, 3, 4)) }
  }
}