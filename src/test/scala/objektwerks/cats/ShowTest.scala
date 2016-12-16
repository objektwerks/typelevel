package objektwerks.cats

import org.scalatest.FunSuite

case class Actor(name: String)

object Actor {
  import cats.Show
  implicit val actorShow: Show[Actor] = Show.show[Actor](_.name)
}

class ShowTest extends FunSuite {
  test("instances") {
    import cats.Show
    import cats.instances.int._
    
    val showInt = Show[Int]
    assert(showInt.show(1) == "1")
  }
  
  test("syntax") {
    import cats.instances.int._
    import cats.syntax.show._
    
    assert(1.show == "1")
  }
  
  test("custom") {
    import cats.syntax.show._
    val actor = Actor("Fred Flintstone")
    assert(actor.show == "Fred Flintstone")
  }
}