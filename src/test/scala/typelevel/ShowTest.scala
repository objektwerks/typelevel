package typelevel

import org.scalatest.FunSuite

class ShowTest extends FunSuite {
  test("instances") {
    import cats.Show
    import cats.instances.int._
    
    val showInt = Show[Int]
    assert(showInt.show(1) == "1")
  }
}