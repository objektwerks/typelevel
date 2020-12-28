package objektwerks.fs2

import cats.effect.IO

import fs2.Stream

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class Fs2Test extends AnyFunSuite with Matchers {
  test("pure") {
    Stream.empty.toList shouldBe List()

    Stream.emit(1).toList shouldBe List(1)
    Stream.emits(List(1, 2, 3)).toList shouldBe List(1, 2, 3)
    Stream.emits(List(1, 2, 3)).toList.sum shouldBe 6

    (Stream(1, 2, 3) ++ Stream(4, 5, 6)).toList shouldBe List(1, 2, 3, 4, 5, 6)
    Stream(Some(1), None, Some(3)).collect { case Some(i) => i }.toList shouldBe List(1, 3)

    Stream(1, 2, 3).filter(_ % 2 == 0).toList shouldBe List(2)
    Stream(1, 2, 3).fold(0)(_ + _).toList shouldBe List(6)
    Stream(1, 2, 3).map(i => i * i).toList shouldBe List(1, 4, 9)
    Stream(1, 2, 3).flatMap(i => Stream(i, i)).toList shouldBe List(1, 1, 2, 2, 3, 3)
  }

  test("eval") {
    Stream.eval( IO { 1 + 2 + 3 } ).compile.toList.unsafeRunSync() shouldBe List(6)
    Stream.eval( IO { 1 + 2 + 3 } ).compile.fold(0)(_ + _).unsafeRunSync() shouldBe 6
    ( Stream(1, 2) ++ Stream.eval( IO.pure(3) ) ).compile.toList.unsafeRunSync() shouldBe List(1, 2, 3)
  }
}