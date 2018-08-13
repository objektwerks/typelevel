name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.6"
libraryDependencies ++= {
  val catsVersion = "1.2.0"
  val doobieVersion = "0.5.3"
  val http4sVersion = "0.18.15"
  val circeVersion = "0.9.3"
  Seq(
    "org.typelevel" % "cats-core_2.12" % catsVersion,
    "org.typelevel" % "cats-free_2.12" % catsVersion,
    "org.typelevel" % "cats-effect_2.12" % "0.10.1",
    "io.circe" % "circe-core_2.12" % circeVersion,
    "io.circe" % "circe-generic_2.12" % circeVersion,
    "io.monix" % "monix_2.12" % "2.3.3",
    "com.chuusai" % "shapeless_2.12" % "2.3.3",
    "org.tpolecat" % "doobie-core_2.12" % doobieVersion,
    "org.tpolecat" % "doobie-h2_2.12" % doobieVersion,
    "org.http4s" % "http4s-blaze-client_2.12" % http4sVersion,
    "org.http4s" % "http4s-blaze-server_2.12" % http4sVersion,
    "org.http4s" % "http4s-circe_2.12" % http4sVersion,
    "org.http4s" % "http4s-dsl_2.12" % http4sVersion,
    "co.fs2" % "fs2-core_2.12" % "0.10.5",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
  )
}
scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-feature",
  "-Ywarn-unused-import",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings",
  "-Xlint:missing-interpolator",
  "-Xlint"
)
