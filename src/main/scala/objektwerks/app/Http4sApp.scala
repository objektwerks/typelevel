package objektwerks.app

import java.time.LocalTime

import cats.data.Kleisli
import cats.effect._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global

case class Now(time: String = LocalTime.now.toString)

object Now {
  implicit val nowDecoder = jsonOf[IO, Now]
}

object Services {
  private val noCacheHeader = Header("Cache-Control", "no-cache, no-store, must-revalidate")

  private def addHeader(service: HttpService[IO], header: Header): HttpService[IO] = Kleisli { request: Request[IO] =>
    service(request).map {
      case Status.Successful(response) => response.putHeaders(header)
      case response => response
    }
  }
  private val indexService = HttpService[IO] {
    case request @ GET -> Root => StaticFile.fromResource("/index.html", Some(request)).getOrElseF(NotFound())
  }
  val indexServiceWithNoCacheHeader = addHeader(indexService, noCacheHeader)

  private val resourceService = HttpService[IO] {
    case request @ GET -> Root / path if List(".ico", ".png", ".css", ".js")
      .exists(path.endsWith) => StaticFile.fromResource("/" + path, Some(request))
      .getOrElseF(NotFound())
  }
  val resourceServiceWithNoCacheHeader = addHeader(resourceService, noCacheHeader)

  val nowService = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }
}

object Http4sApp extends StreamApp[IO] {
  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    sys.addShutdownHook(requestShutdown.unsafeRunSync)
    BlazeBuilder[IO]
      .bindHttp(7777)
      .mountService(Services.indexServiceWithNoCacheHeader, "/")
      .mountService(Services.resourceServiceWithNoCacheHeader, "/")
      .mountService(Services.nowService, "/api/v1")
      .serve
  }
}