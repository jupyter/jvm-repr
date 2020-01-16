
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

// sbt-dynver doesn't generate correct versions, because tags don't have a
// 'v' prefix, so we're overriding its logic here
version := {
  import sys.process._
  val describe = Seq("git", "describe", "--tags").!!.trim
  val latestTag = Seq("git", "describe", "--tags", "--abbrev=0").!!.trim
  if (describe == latestTag)
    describe
  else
    describe + "-SNAPSHOT"
}
