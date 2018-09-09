package objektwerks.monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration._

class MonixTest extends FunSuite with Matchers {
  test("run async task") {
    val task = Task { 1 + 2 }
    val cancelable = task.runAsync
    cancelable.onComplete(x => x.get shouldBe 3)
  }

  test("run on complete task") {
    val task = Task { 1 + 2 }
    val cancellable = task.runAsync
    cancellable.onComplete(x => x.get shouldBe 3)
  }

  test("run async observable task") {
    val task = {
      Observable.interval(1.second)
        .filter(_ % 2 == 0)
        .map(_ * 2)
        .flatMap(x => Observable.fromIterable(Seq(x, x)))
        .take(3)
        .toListL
    }
    val cancelable = task.runAsync
    cancelable.onComplete(xs => xs.get.sum shouldBe 4)
  }

  test("coeval task") {
    val task = Task.eval{ 1 + 2 }
    val evaluating = task.coeval
    evaluating.value.getOrElse(-1) shouldBe 3
  }
}