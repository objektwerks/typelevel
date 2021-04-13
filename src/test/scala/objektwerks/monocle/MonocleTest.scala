package objektwerks.monocle

import monocle.Lens
import monocle.macros.GenLens

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

case class Resident(name: String, street: Street)
case class Street(name: String, city: City)
case class City(name: String, state: State)
case class State(name: String)

class MonocleTest extends AnyFunSuite with Matchers {
  val resident = Resident("fred flintstone", Street("1 rock st", City("boulder", State("co"))))

  test("copy") {
    ( resident.name != resident.copy(name = "barney rebel") ) shouldBe true
    ( resident.street != resident.street.copy(name = "3 stoney dr") ) shouldBe true
    ( resident.street.city != resident.street.city.copy(name = "denver") ) shouldBe true
    ( resident.street.city.state != resident.street.city.state.copy(name = "colorado") ) shouldBe true
  }
  
  test("lens") {
    val residentLens: Lens[Resident, Street] = GenLens[Resident](_.street)
    val streetLens: Lens[Street, City] = GenLens[Street](_.city)
    val cityLens: Lens[City, State] = GenLens[City](_.state)

    GenLens[Resident](_.name).set("barney rebel")(resident) shouldBe
    Resident("barney rebel", Street("1 rock st", City("boulder", State("co"))))

    residentLens.set( Street("3 stoney dr", City("denver", State("co"))) )(resident) shouldBe
    Resident("fred flintstone", Street("3 stoney dr", City("denver", State("co"))))

    ( residentLens.composeLens(streetLens) ).set( City("denver", State("co")) )(resident) shouldBe
    Resident("fred flintstone", Street("1 rock st", City("denver", State("co"))))

    ( residentLens.composeLens(streetLens).composeLens(cityLens) ).set( State("colorado") )(resident) shouldBe
    Resident("fred flintstone", Street("1 rock st", City("boulder", State("colorado"))))
  }
}