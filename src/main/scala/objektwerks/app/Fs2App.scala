package objektwerks.app

import cats.effect.{Blocker, ExitCode, IO, IOApp}

import fs2.{io, text, Stream}

import java.nio.file.Paths

object Fs2App extends IOApp {
  val reader: Stream[IO, Unit] = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file.readAll[IO](Paths.get(".gitignore"), blocker, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .through(text.utf8Encode)
      .through(io.file.writeAll(Paths.get("target/gitignore"), blocker))
  }

  def run(args: List[String]): IO[ExitCode] = reader.compile.drain.as(ExitCode.Success)
}