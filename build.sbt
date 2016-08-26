name := "typelevel"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.8"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies ++= {
  Seq(
    "org.typelevel" % "cats-core_2.11" % "0.7.0",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
  )
}
scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xfatal-warnings"
)
fork in test := true
javaOptions += "-server -Xss1m -Xmx2g"
logLevel := Level.Info
