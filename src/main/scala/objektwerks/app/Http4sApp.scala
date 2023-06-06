package objektwerks.app

import java.time.LocalTime
import java.util.concurrent.Executors

import cats.data.Kleisli
import cats.effect._

import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.Status.Successful

import scala.concurrent.ExecutionContext

case class Now(time: String = LocalTime.now.toString)

object Now {
  implicit val nowDecoder: EntityDecoder[IO, Now] = jsonOf[IO, Now]
}

object Headers {
  val noCacheHeader = Header("Cache-Control", "no-cache, no-store, must-revalidate")

  def addHeader(route: HttpRoutes[IO], header: Header): HttpRoutes[IO] = Kleisli { request: Request[IO] =>
    route(request).map {
      case Successful(response) => response.putHeaders(header)
      case response => response
    }
  }
}

object Routes {
  import Headers._

  val blockingExecutionContext = Blocker.liftExecutionContext( ExecutionContext.fromExecutorService( Executors.newFixedThreadPool(1) ) )
  implicit val contextShift: ContextShift[IO] = IO.contextShift( ExecutionContext.global )

  val indexRoute = HttpRoutes.of[IO] {
    case request @ GET -> Root => StaticFile.fromResource("/index.html", blockingExecutionContext, Some(request))
                                            .getOrElseF( NotFound() )
  }
  val indexRouteWithNoCacheHeader = addHeader(indexRoute, noCacheHeader)

  val resourceRoute = HttpRoutes.of[IO] {
    case request @ GET -> Root / path if List(".ico", ".css", ".js")
      .exists(path.endsWith) => StaticFile.fromResource("/" + path, blockingExecutionContext, Some(request))
      .getOrElseF( NotFound() )
  }
  val resourceRouteWithNoCacheHeader = addHeader(resourceRoute, noCacheHeader)

  val nowRoute = HttpRoutes.of[IO] {
    case GET -> Root / "now" => Ok( Now().asJson )
  }

  val routes = Router("" -> indexRouteWithNoCacheHeader,
                      "/" -> resourceRouteWithNoCacheHeader,
                      "/api/v1" -> nowRoute).orNotFound
}

object Http4sApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]( executionContext )
      .bindHttp(7777, "localhost")
      .withHttpApp( Routes.routes )
      .serve
      .compile
      .drain
      .as( ExitCode.Success )
}