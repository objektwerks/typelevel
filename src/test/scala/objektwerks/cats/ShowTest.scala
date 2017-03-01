package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

case class Actor(name: String)

object Actor {
  import cats.Show
  implicit val actorShow: Show[Actor] = Show.show[Actor](_.name)
}

class ShowTest extends FunSuite with Matchers {
  test("instances") {
    import cats.Show
    import cats.instances.int._
    
    val showInt = Show[Int]
    showInt.show(1) shouldEqual "1"
  }
  
  test("syntax") {
    import cats.instances.int._
    import cats.syntax.show._
    
    1.show shouldEqual "1"
  }
  
  test("custom") {
    import cats.syntax.show._
    
    val actor = Actor("Fred Flintstone")
    actor.show shouldEqual "Fred Flintstone"
  }
}