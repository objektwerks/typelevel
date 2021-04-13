package objektwerks.monocle

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

case class Resident(name: String, street: Street)
case class Street(name: String, city: City)
case class City(name: String, state: State)
case class State(name: String)

class MonocleTest extends AnyFunSuite with Matchers {
  test("lens") {
  }

  test("prisms") {
  }

  test("optics") {
  }
}