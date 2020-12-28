package objektwerks.fs2

import fs2.Stream

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class Fs2Test extends AnyFunSuite with Matchers {
  test("pure") {
    Stream.empty.toList shouldBe List()
    Stream.emit(1).toList shouldBe List(1)
    Stream.emits(List(1, 2, 3)).toList shouldBe List(1, 2, 3)
    Stream.emits(List(1, 2, 3)).toList.sum shouldBe 6
  }
}