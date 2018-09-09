name := """Project1"""
organization := "NCSU"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
//adding jackson library for json parsing
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"

//adding json library for the json.
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"




libraryDependencies += javaForms

libraryDependencies += javaJdbc

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.23.1"