name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.7"
libraryDependencies ++= {
  val catsVersion = "1.4.0"
  val doobieVersion = "0.6.0"
  val http4sVersion = "0.20.0-M2"
  val circeVersion = "0.10.0"
  Seq(
    "org.typelevel" % "cats-core_2.12" % catsVersion,
    "org.typelevel" % "cats-free_2.12" % catsVersion,
    "org.typelevel" % "cats-effect_2.12" % "1.0.0",
    "io.circe" % "circe-core_2.12" % circeVersion,
    "io.circe" % "circe-generic_2.12" % circeVersion,
    "io.monix" % "monix_2.12" % "2.3.3",
    "com.chuusai" % "shapeless_2.12" % "2.3.3",
    "org.tpolecat" % "doobie-core_2.12" % doobieVersion,
    "org.tpolecat" % "doobie-h2_2.12" % doobieVersion,
    "org.http4s" % "http4s-blaze-client_2.12" % http4sVersion,
    "org.http4s" % "http4s-blaze-server_2.12" % http4sVersion,
    "org.http4s" % "http4s-dsl_2.12" % http4sVersion,
    "org.http4s" % "http4s-circe_2.12" % http4sVersion,
    "co.fs2" % "fs2-core_2.12" % "1.0.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
  )
}