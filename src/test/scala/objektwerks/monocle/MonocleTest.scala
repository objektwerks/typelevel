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
    val resident: Lens[Resident, Street] = GenLens[Resident](_.street)
    val street: Lens[Street, City] = GenLens[Street](_.city)
    val city: Lens[City, State] = GenLens[City](_.state)
}

class MonocleTest extends AnyFunSuite with Matchers {
  test("copy") {

  }
  
  test("lens") {
  }

  test("prisms") {
  }

  test("optics") {
  }
}