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
  val stateLens: Lens[State, String] = GenLens[State](_.name)
}

class MonocleTest extends AnyFunSuite with Matchers {
  import ResidentLens._

  val resident = Resident("fred flintstone", Street("1 rock st", City("boulder", State("co"))))

  test("copy") {
    val state = resident.street.city.state
    val newState = resident.street.city.state.copy(name = "colorado")
    ( state != newState ) shouldBe true
  }
  
  test("lens") {
    residentLens.set( Street("3 stoney dr", City("denver", State("co"))) )(resident) shouldBe
    Resident("fred flintstone", Street("3 stoney dr", City("denver", State("co"))))

    ( residentLens.composeLens(streetLens) ).set( City("denver", State("co")) )(resident) shouldBe
    Resident("fred flintstone", Street("1 rock st", City("denver", State("co"))))

    ( residentLens.composeLens(streetLens).composeLens(cityLens) ).set( State("colorado") )(resident) shouldBe
    Resident("fred flintstone", Street("1 rock st", City("boulder", State("colorado"))))
  }
}