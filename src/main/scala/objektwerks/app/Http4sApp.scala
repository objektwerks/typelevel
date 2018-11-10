package objektwerks.app

import java.time.LocalTime
import java.util.concurrent.Executors

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext

case class Now(time: String = LocalTime.now.toString)

object Now {
  implicit val nowDecoder = jsonOf[IO, Now]
}

object Headers {
  val noCacheHeader = Header("Cache-Control", "no-cache, no-store, must-revalidate")

  def addHeader(route: HttpRoutes[IO], header: Header): HttpRoutes[IO] = Kleisli { request: Request[IO] =>
    route(request).map {
      case Status.Successful(response) => response.putHeaders(header)
      case response => response
    }
  }
}

object Routes {
  import Headers._

  private val blockingEc = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))
  private implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  private val indexRoute = HttpRoutes.of[IO] {
    case request @ GET -> Root => StaticFile.fromResource("/index.html", blockingEc, Some(request))
      .getOrElseF(NotFound())
  }
  private val indexRouteWithNoCacheHeader = addHeader(indexRoute, noCacheHeader)

  private val resourceRoute = HttpRoutes.of[IO] {
    case request @ GET -> Root / path if List(".ico", ".css", ".js")
      .exists(path.endsWith) => StaticFile.fromResource("/" + path, blockingEc, Some(request))
      .getOrElseF(NotFound())
  }
  private val resourceRouteWithNoCacheHeader = addHeader(resourceRoute, noCacheHeader)

  private val nowRoute = HttpRoutes.of[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }

  val routes = Router("" -> indexRouteWithNoCacheHeader,
                      "/" -> resourceRouteWithNoCacheHeader,
                      "/api/v1" -> nowRoute).orNotFound
}

object Http4sApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(7777, "localhost")
      .withHttpApp(Routes.routes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}