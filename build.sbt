
inThisBuild(List(
  organization := "org.jupyter",
  homepage := Some(url("https://github.com/jupyter/jvm-repr")),
  licenses := List("BSD-3-Clause" -> url("https://opensource.org/licenses/BSD-3-Clause")),
  developers := List(
    Developer(
      "jvm-repr",
      "jvm-repr contributors",
      "",
      url("https://github.com/jupyter/jvm-repr/graphs/contributors")
    )
  )
))

name := "jvm-repr"

// pure java project
autoScalaLibrary := false
crossVersion := CrossVersion.disabled

// test stuff
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
fork.in(Test) := true // seems required for the tests to run fine
