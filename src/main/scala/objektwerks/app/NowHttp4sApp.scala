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

case class Now(time: String = LocalTime.now.toString)
object Now {
  implicit val nowDecoder = jsonOf[IO, Now]
}

object NowService {
  val service = HttpService[IO] {
    case GET -> Root / "now" => Ok(Now().asJson)
  }
}

object NowHttp4sApp extends StreamApp[IO] {
  import NowService._

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO].bindHttp(7777).mountService(service, "/").serve
}