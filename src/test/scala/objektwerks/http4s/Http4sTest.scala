package objektwerks.http4s

import java.time.LocalTime

import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.blaze.Http1Client
import org.http4s.dsl.io._
import org.http4s.server.blaze.BlazeBuilder
import org.scalatest.{BeforeAndAfterAll, FunSuite}

case class Now(time: String = LocalTime.now.toString)
object Now {
  implicit val nowDecoder = jsonOf[IO, Now]
}

class Http4sTest extends FunSuite with BeforeAndAfterAll {
  val service = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }

  val builder = BlazeBuilder[IO].bindHttp(7979).mountService(service, "/").start
  val server = builder.unsafeRunSync

  val client = Http1Client[IO]().unsafeRunSync()

  override protected def afterAll(): Unit = {
    server.shutdownNow()
    client.shutdownNow()
  }

  test("get") {
    val get = client.expect[Now]("http://localhost:7979/now")
    val now = get.unsafeRunSync()
    assert(now.time.nonEmpty)
    println(s"The time is: ${now.time}")
  }
}