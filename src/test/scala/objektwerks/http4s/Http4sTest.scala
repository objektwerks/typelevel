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

case class Message(text: String)
object Message {
  implicit val messageDecoder = jsonOf[IO, Message]
}

object Services {
  val nowService = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }
  val messageService = HttpService[IO] {
    case request @ POST -> Root / "message" => for {
      message <- request.as[Message]
      response <- Ok(Message(s"client: ${message.text}  server: Cheers!").asJson)
    } yield response
  }
}

class Http4sTest extends FunSuite with BeforeAndAfterAll {
  import Services._

  val server = BlazeBuilder[IO]
    .bindHttp(7979)
    .mountService(nowService, "/")
    .mountService(messageService, "/")
    .start
    .unsafeRunSync
  val client = Http1Client[IO]().unsafeRunSync

  override protected def afterAll(): Unit = {
    server.shutdownNow
    client.shutdownNow
  }

  test("client-server get") {
    val get = Request[IO](Method.GET, uri("http://localhost:7979/now"))
    val now = client.expect[Now](get).unsafeRunSync
    assert(now.time.nonEmpty)
    println(s"current time: ${now.time}")
  }

  test("client-server post") {
    val post = Request[IO](Method.POST, uri("http://localhost:7979/message")).withBody(Message("Prost!").asJson)
    val message = client.expect[Message](post).unsafeRunSync
    assert(message.text.nonEmpty)
    println(message.text)
  }

  test("serverless get") {
    val get = Request[IO](Method.GET, uri("/now"))
    val io = nowService.orNotFound.run(get)
    val now = io.unsafeRunSync().as[Now].unsafeRunSync
    assert(now.time.nonEmpty)
    println(s"current time: ${now.time}")
  }

  test("serverless post") {
    val post = Request[IO](Method.POST, uri("/message")).withBody(Message("Prost!").asJson).unsafeRunSync()
    val io = messageService.orNotFound.run(post)
    val message = io.unsafeRunSync().as[Message].unsafeRunSync
    assert(message.text.nonEmpty)
    println(message.text)
  }
}