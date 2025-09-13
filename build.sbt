val appVersion:String = "0.1"
val globalScalaVersion = "3.3.6"

ThisBuild / organization := "ai.dragonfly"
ThisBuild / organizationName := "dragonfly.ai"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List( tlGitHubDev("dragonfly-ai", "dragonfly.ai") )
ThisBuild / scalaVersion := globalScalaVersion

ThisBuild / tlSitePublishBranch := Some("main")

ThisBuild / tlBaseVersion := appVersion
ThisBuild / tlCiReleaseBranches := Seq()

ThisBuild / nativeConfig ~= {
  _.withLTO(scala.scalanative.build.LTO.thin)
   .withMode(scala.scalanative.build.Mode.releaseFast)
   .withGC(scala.scalanative.build.GC.commix)
}

lazy val uriel = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .settings(
    name := "uriel",
    description := "A Color Science library for Scala, Scala.js, and Scala Native.",
    Compile / mainClass := Some("ai.dragonfly.uriel.verification.ConversionFidelity"),
    libraryDependencies ++= Seq(
      "ai.dragonfly" %%% "mesh" % "0.14",
      "ai.dragonfly" %%% "spatial" % "0.1"
    )
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := true
  )
  .jvmSettings()

lazy val root = tlCrossRootProject.aggregate(uriel, tests).settings(name := "uriel")

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin).settings(
  mdocVariables := Map(
    "VERSION" -> appVersion,
    "SCALA_VERSION" -> globalScalaVersion
  ),
  laikaConfig ~= { _.withRawContent }
)

lazy val unidocs = project
  .in(file("unidocs"))
  .enablePlugins(TypelevelUnidocPlugin) // also enables the ScalaUnidocPlugin
  .settings(
    name := "uriel-docs",
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inProjects(
        uriel.jvm,
        uriel.js,
        uriel.native
      )
  )

lazy val tests = crossProject(
    JVMPlatform,
    JSPlatform,
    NativePlatform
  )
  .in(file("tests"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(uriel)
  .settings(
    name := "uriel-tests",
    libraryDependencies += "org.scalameta" %%% "munit" % "1.1.2" % Test
  )