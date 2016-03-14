name := "objektwerks.typelevel"
version := "1.0"
scalaVersion := "2.11.8"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies ++= {
  Seq(
    "org.typelevel" % "cats-core_2.11" % "0.4.1",
    "com.chuusai" %% "shapeless" % "2.3.0",
    "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
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
