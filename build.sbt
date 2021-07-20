name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.13.6"
libraryDependencies ++= {
  val catsVersion = "2.6.1"
  val catsEffectVersion = "3.1.1"
  val doobieVersion = "0.13.4"
  val http4sVersion = "0.21.22"
  val circeVersion = "0.13.0"
  val monocleVersion = "2.1.0"
  val refinedVersion = "0.9.23"
  val shapelessVersion = "2.3.4"
  Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-free" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-h2" % doobieVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
    "eu.timepit" %% "refined" % refinedVersion,
    "com.chuusai" %% "shapeless" % shapelessVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.9" % Test
  )
}
