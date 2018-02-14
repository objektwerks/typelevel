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

class Http4sTest extends FunSuite with BeforeAndAfterAll {
  val service = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
    case request @ POST -> Root / "message" => for {
      message <- request.as[Message]
      response <- Ok(Message(s"client: ${message.text}  server: Cheers!").asJson)
    } yield response
  }

  val builder = BlazeBuilder[IO].bindHttp(7979).mountService(service, "/").start
  val server = builder.unsafeRunSync

  val client = Http1Client[IO]().unsafeRunSync()

  override protected def afterAll(): Unit = {
    server.shutdownNow()
    client.shutdownNow()
  }

  test("client-server get") {
    val get = Request[IO](Method.GET, uri("http://localhost:7979/now"))
    val io = client.expect[Now](get)
    val now = io.unsafeRunSync()
    assert(now.time.nonEmpty)
    println(s"current time: ${now.time}")
  }

  test("client-server post") {
    val post = Request[IO](Method.POST, uri("http://localhost:7979/message")).withBody(Message("Prost!").asJson)
    val io = client.expect[Message](post)
    val message = io.unsafeRunSync()
    assert(message.text.nonEmpty)
    println(message.text)
  }

  test("serverless get") {
    val get = Request[IO](Method.GET, uri("/now"))
    val id = service.orNotFound.run(get)
    val now = id.unsafeRunSync().as[Now].unsafeRunSync()
    assert(now.time.nonEmpty)
    println(s"current time: ${now.time}")
  }

  test("serverless post") {
    val post = Request[IO](Method.POST, uri("/message")).withBody(Message("Prost!").asJson).unsafeRunSync()
    val io = service.orNotFound.run(post)
    val message = io.unsafeRunSync().as[Message].unsafeRunSync()
    assert(message.text.nonEmpty)
    println(message.text)
  }
}