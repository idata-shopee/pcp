name := "pcp"
organization := "io.github.lock-free"
version := "0.1.1"
scalaVersion := "2.12.4"

useGpg := true 
parallelExecution in Test := true

publishTo := sonatypePublishTo.value

libraryDependencies ++= Seq(
  // log lib
  "io.github.lock-free" %% "klog" % "0.1.0",

  // JSON lib
  "io.github.lock-free" %% "sjson" % "0.2.0",

  // test suite
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)
