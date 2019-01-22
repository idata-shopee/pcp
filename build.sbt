name := "pcp"
organization := "io.github.idata-shopee"
version := "0.1.1"
scalaVersion := "2.12.4"

useGpg := true 
parallelExecution in Test := true

publishTo := sonatypePublishTo.value

libraryDependencies ++= Seq(
  // log lib
  "io.github.idata-shopee" %% "klog" % "0.1.0",

  // JSON lib
  "io.github.idata-shopee" %% "sjson" % "0.1.5",

  // test suite
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)
