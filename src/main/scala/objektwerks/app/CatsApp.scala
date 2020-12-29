package objektwerks.app

import cats.effect.{ExitCode, IO, IOApp}

object CatsApp extends IOApp { // TODO!
  def run(args: List[String]): IO[ExitCode] = IO { ExitCode.Success }
}