package objektwerks.monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class TaskTest extends FunSuite with Matchers {
  test("task") {
    val task = Task { 1 + 2 }
    val future = task.runAsync
    Await.result(future, 100.millis) shouldBe 3
  }

  test("observable task") {
    val task = {
      Observable.interval(1.second)
        .filter(_ % 2 == 0)
        .map(_ * 2)
        .flatMap(x => Observable.fromIterable(Seq(x, x)))
        .take(3)
        .toListL
    }
    val future = task.runAsync
    Await.result(future, 3 seconds).sum shouldBe 4
  }
}