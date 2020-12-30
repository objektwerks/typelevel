package objektwerks.app

import cats.effect.{ExitCode, IO, IOApp}

import java.time.{LocalDate, LocalTime}

object CatsApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    IO {
      println(s"*** Date: ${LocalDate.now()} Time: ${LocalTime.now()} ***")
    }.as(ExitCode.Success)
}