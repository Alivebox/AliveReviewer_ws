organization := "com.alivebox.patchviewer"

name := "patchviewer"

version := "0.1-SNAPSHOT"

autoScalaLibrary := false

scalaVersion := "2.10.2"

seq(webSettings :_*)

libraryDependencies ++= {
  val liftVersion = "2.5"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "net.liftweb" % "lift-mapper_2.10" % "2.5",
    "org.scala-lang" % "scala-reflect" % "2.10.0",
    "mysql" % "mysql-connector-java" % "5.1.25",
    "log4j" % "log4j" % "1.2.17",
    "org.slf4j" % "slf4j-log4j12" % "1.7.5",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910"  % "compile,container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" %
      "container,compile" artifacts Artifact("javax.servlet", "jar", "jar")
  )
}