package objektwerks.app

import java.time.LocalTime

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

object Headers {
  val noCacheHeader = Header("Cache-Control", "no-cache, no-store, must-revalidate")

  def addHeader(response: Response[IO], header: Header) = response match {
    case Status.Successful(r) => r.putHeaders(header)
    case r => r
  }

  def apply(service: HttpService[IO], header: Header) = service.map(addHeader(_, header))
}

object Services {
  import Headers._

  val indexService = HttpService[IO] {
    case _ @ GET -> Root => Ok("/index.html")
  }
  val indexServiceWithNoCacheHeader = Headers(indexService, noCacheHeader)

  val resourceService = HttpService[IO] {
    case request @ GET -> Root / path if List(".css", ".js", ".ico", ".appcache").exists(path.endsWith) =>
      StaticFile.fromResource("/" + path, Some(request)).getOrElseF(NotFound())
  }
  val resourceServiceWithNoCacheHeader = Headers(resourceService, noCacheHeader)

  val nowService = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }
}

object NowHttp4sApp extends StreamApp[IO] {
  import Services._

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(7777)
      .mountService(indexServiceWithNoCacheHeader, "/")
      .mountService(resourceServiceWithNoCacheHeader, "/")
      .mountService(nowService, "/api/v1")
      .serve
}