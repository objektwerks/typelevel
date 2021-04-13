package objektwerks.monocle

import monocle.Lens
import monocle.macros.GenLens

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

case class Resident(name: String, street: Street)
case class Street(name: String, city: City)
case class City(name: String, state: State)
case class State(name: String)

object ResidentLens {
  val residentLens: Lens[Resident, Street] = GenLens[Resident](_.street)
  val streetLens: Lens[Street, City] = GenLens[Street](_.city)
  val cityLens: Lens[City, State] = GenLens[City](_.state)
}

class MonocleTest extends AnyFunSuite with Matchers {
  val resident = Resident("fred flintstone", Street("1 rock st", City("boulder", State("co"))))
  
  test("copy") {

  }
  
  test("lens") {
  }

  test("prisms") {
  }

  test("optics") {
  }
}