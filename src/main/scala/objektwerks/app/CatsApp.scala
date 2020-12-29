package objektwerks.app

import cats.effect.{ExitCode, IO, IOApp}

import java.time.ZonedDateTime

object CatsApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    IO {
      println(s"The current date and time is: ${ZonedDateTime.now()}")
    }.as(ExitCode.Success)
}