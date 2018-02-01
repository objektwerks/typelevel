name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.4"
libraryDependencies ++= {
  val catsVersion = "1.0.1"
  val doobieVersion = "0.4.4"
  Seq(
    "org.typelevel" % "cats-core_2.12" % catsVersion,
    "org.typelevel" % "cats-free_2.12" % catsVersion,
    "io.monix" % "monix_2.12" % "2.3.3",
    "com.chuusai" % "shapeless_2.12" % "2.3.3",
    "org.tpolecat" % "doobie-core-cats_2.12" % doobieVersion,
    "org.tpolecat" % "doobie-h2-cats_2.12" % doobieVersion,
    "com.h2database" % "h2" % "1.4.196",
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
