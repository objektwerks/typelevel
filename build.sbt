name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.8"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies ++= {
  Seq(
    "org.typelevel" % "cats-core_2.11" % "0.7.2",
    "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0",
    "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
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