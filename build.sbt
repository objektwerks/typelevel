name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.4"
libraryDependencies ++= {
  val catsVersion = "1.0.1"
  val doobieVersion = "0.5.0-M14"
  val http4sVersion = "0.18.0"
  Seq(
    "org.typelevel" % "cats-core_2.12" % catsVersion,
    "org.typelevel" % "cats-free_2.12" % catsVersion,
    "io.circe" %% "circe-generic" % "0.9.1",
    "io.monix" % "monix_2.12" % "2.3.3",
    "com.chuusai" % "shapeless_2.12" % "2.3.3",
    "org.tpolecat" % "doobie-core_2.12" % doobieVersion,
    "org.tpolecat" % "doobie-h2_2.12" % doobieVersion,
    "org.http4s" % "http4s-blaze-client_2.12" % http4sVersion,
    "org.http4s" % "http4s-blaze-server_2.12" % http4sVersion,
    "org.http4s" % "http4s-circe_2.12" % http4sVersion,
    "org.http4s" % "http4s-dsl_2.12" % http4sVersion,
    "com.typesafe.scala-logging" % "scala-logging_2.12" % "3.7.2",
    "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
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
fork in test := true
javaOptions += "-server -Xss1m -Xmx2g"
