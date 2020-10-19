package objektwerks.http4s

import java.time.LocalTime

import cats.effect._

import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.http4s.client.blaze.BlazeClientBuilder
import org.scalatest.matchers.should.Matchers

case class Now(time: String = LocalTime.now.toString)
object Now {
  implicit val nowDecoder = jsonOf[IO, Now]
}

case class Message(text: String)
object Message {
  implicit val messageDecoder = jsonOf[IO, Message]
}

object Routes {
  val nowRoute = HttpRoutes.of[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }
  val messageRoute = HttpRoutes.of[IO] {
    case request @ POST -> Root / "message" => for {
      message <- request.as[Message]
      response <- Ok(Message(s"client: ${message.text}  server: Cheers!").asJson)
    } yield response
  }
  val routes = Router("/now" -> nowRoute, "/message" -> messageRoute).orNotFound
}

object HttpApp {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  lazy val server = BlazeServerBuilder[IO](global)
    .bindHttp(7979, "localhost")
    .withHttpApp(Routes.routes)
    .resource.use(_ => IO.never)
    .start
    .unsafeRunSync()
}

class Http4sTest extends AnyFunSuite with BeforeAndAfterAll with Matchers {
  import scala.concurrent.ExecutionContext.Implicits.global
  import HttpApp._
  import Routes._

  override protected def afterAll(): Unit = {
    server.cancel.unsafeRunSync()
  }

  test("client-server get") {
    val get = Request[IO](Method.GET, Uri.uri("http://localhost:7979/now"))
    BlazeClientBuilder[IO](global).resource.use { client =>
      val now = client.expect[Now](get).unsafeRunSync()
      LocalTime.parse(now.time).isInstanceOf[LocalTime] shouldBe true
      IO.unit
    }
  }

  test("client-server post") {
    val post = Request[IO](Method.POST, Uri.uri("http://localhost:7979/message")).withEntity(Message("Prost!").asJson)
    BlazeClientBuilder[IO](global).resource.use { client =>
      val message = client.expect[Message](post).unsafeRunSync()
      message.text shouldEqual "client: Prost!  server: Cheers!"
      IO.unit
    }
  }

  test("serverless get") {
    val get = Request[IO](Method.GET, Uri.uri("/now"))
    val io = nowRoute.orNotFound.run(get)
    val now = io.unsafeRunSync().as[Now].unsafeRunSync()
    LocalTime.parse(now.time).isInstanceOf[LocalTime] shouldBe true
  }

  test("serverless post") {
    val post = Request[IO](Method.POST, Uri.uri("/message")).withEntity(Message("Prost!").asJson)
    val io = messageRoute.orNotFound.run(post)
    val message = io.unsafeRunSync().as[Message].unsafeRunSync()
    message.text shouldEqual "client: Prost!  server: Cheers!"
  }
}