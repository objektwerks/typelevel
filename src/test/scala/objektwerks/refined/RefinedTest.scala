package objektwerks.refined

import eu.timepit.refined.api._
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RefinedTest extends AnyFunSuite with Matchers {
  test("boolean") {
    val isTrue: Boolean Refined True = true
    isTrue.value shouldBe true
  }

  test("char") {
    val digit: Refined[Char, Digit] = '3'
    digit.value shouldBe '3'

    val letter: Refined[Char, Letter] = 'c'
    letter.value shouldBe 'c'
  }

  test("collection") {
    val nonEmpty: Refined[String, NonEmpty] = "nonempty"
    nonEmpty.value.nonEmpty shouldBe true
  }

  test("numeric") {
    val positive: Refined[Int, Positive] = 3
    positive.value shouldBe 3

    val negative: Refined[Int, Negative] = -3
    negative.value shouldBe -3

    val interval: Refined[Int, Interval.Open[1, 6]] = 3
    interval.value shouldBe 3
  }

  test("string") {
    val url: Refined[String, Url] = "https://www.scala-lang.org"
    url.value shouldBe "https://www.scala-lang.org"

    val uuid: Refined[String, Uuid] = "6804aab6-9c53-11eb-a8b3-0242ac130003"
    uuid.value.nonEmpty shouldBe true
  }
}