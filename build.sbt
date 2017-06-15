name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.2"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies ++= {
  val catsVersion = "0.9.0"
  val doobieVersion = "0.4.1"
  Seq(
    "org.typelevel" % "cats-core_2.12" % catsVersion,
    "org.typelevel" % "cats-free_2.12" % catsVersion,
    "io.monix" % "monix_2.12" % "2.2.2",
    "com.chuusai" % "shapeless_2.12" % "2.3.2",
    "org.tpolecat" % "doobie-core-cats_2.12" % doobieVersion,
    "org.tpolecat" % "doobie-h2-cats_2.12" % doobieVersion,
    "com.h2database" % "h2" % "1.4.193",
    "com.typesafe.scala-logging" % "scala-logging_2.12" % "3.5.0",
    "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"
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
