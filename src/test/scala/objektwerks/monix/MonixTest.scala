package objektwerks.monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class MonixTest extends FunSuite with Matchers {
  test("task") {
    val task = Task { 1 + 2 }
    val future = task.runAsync
    Await.result(future, 100.millis) shouldBe 3
  }
}