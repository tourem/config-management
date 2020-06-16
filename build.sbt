resolvers += "Typesafe repository" at "https://crm-nexus.itg.echonet/repository/public/"

ThisBuild / organization := "io.zengularity"

ThisBuild / scalaVersion := "2.12.10"

ThisBuild / scalacOptions := (if (Settings.isCiBuild) Settings.scalacOptions.strict else Settings.scalacOptions.lenient)
Compile / console / scalacOptions := Settings.scalacOptions.console
// Note : if some warnings appear to be problematic when compiling typical ScalaTest code, we can also override this:
// Test / scalacOptions := ???

ThisBuild / scalafmtOnCompile := true

lazy val root = project
  .in(file("."))
  .aggregate(
    api,
    frontProspect,
    frontBackoffice,
    shared,
    macros,
    migrationCommon,
    migrationV270ToV280,
    hotfixCommon,
    hotfix4,
    dbExporter
  )

lazy val i18nFiles = taskKey[Seq[File]]("The task that generates the object containing i18n files list.")

lazy val api = project
  .in(file("api"))
  .settings(Settings.commonPlay: _*)
  .settings(
    name := "api",
    libraryDependencies ++= Seq(
      ws,
      // persistence
      jdbc,
      evolutions,
      // mails
      Dependencies.playMailer.core,
      Dependencies.playMailer.javaActivationFwk,
      // Thumbnails generation
      Dependencies.scrimage.core,
      Dependencies.scrimage.ioExtra,
      // Macwire
      Dependencies.macWire.macros,
      Dependencies.macWire.util,
      Dependencies.tagging.core,
      Dependencies.alpakka.jms,
      // CSV handling
      Dependencies.alpakka.csv
    ),
    (Compile / i18nFiles) := {
      val resourceDir   = (Compile / resourceDirectory).value
      val srcManagedDir = (Compile / sourceManaged).value
      Tasks.generateI18nResourcePathsObject(resourceDir, srcManagedDir)
    },
    (Compile / sourceGenerators) += (Compile / i18nFiles)
  )
  .enablePlugins(PlayScala, BuildInfoPlugin)
  .dependsOn(
    shared % "compile->compile;test->test",
    oracleLibs
  )

lazy val frontProspect = project
  .in(file("front-prospect"))
  .settings(Settings.commonPlayFront: _*)
  .settings(
    name := "front-prospect",
    libraryDependencies ++= Seq(
      // cache
      ehcache,
      // pac4j + saml extension
      Dependencies.pac4j.core,
      Dependencies.pac4j.saml,
      Dependencies.pac4j.play,
      // Macwire
      Dependencies.macWire.macros
    ),
    watchSources := Tasks.filterOutPublicDir(baseDirectory.value, watchSources.value),
    PlayKeys.playMonitoredFiles := PlayKeys.playMonitoredFiles.value
      .filterNot(file => file.getAbsolutePath.contains(s"${baseDirectory.value}/public")),
    PlayKeys.devSettings := Seq("play.server.http.port" -> "9002"),
    pipelineStages := Seq(digest)
  )
  .enablePlugins(SbtWeb, PlayScala, BuildInfoPlugin)
  .dependsOn(shared % "compile->compile;test->test")

lazy val frontBackoffice = project
  .in(file("front-backoffice"))
  .settings(Settings.commonPlayFront: _*)
  .settings(
    name := "front-backoffice",
    libraryDependencies ++= Seq(
      // cache
      ehcache,
      // pac4j + saml extension
      Dependencies.pac4j.core,
      Dependencies.pac4j.saml,
      Dependencies.pac4j.play,
      // Macwire
      Dependencies.macWire.macros
    ),
    watchSources := Tasks.filterOutPublicDir(baseDirectory.value, watchSources.value),
    PlayKeys.playMonitoredFiles := PlayKeys.playMonitoredFiles.value
      .filterNot(file => file.getAbsolutePath.contains(s"${baseDirectory.value}/public")),
    PlayKeys.devSettings := Seq("play.server.http.port" -> "9001"),
    pipelineStages := Seq(digest)
  )
  .enablePlugins(SbtWeb, PlayScala, BuildInfoPlugin)
  .dependsOn(shared % "compile->compile;test->test")

lazy val macros = project
  .in(file("macros"))
  .settings(
    name := "macros",
    libraryDependencies ++= Seq(
      Dependencies.playJson.core,
      Dependencies.scalaLang.scalaReflect(scalaVersion.value),
      // Test dependencies
      Dependencies.scalatest.core % Test
    )
  )

lazy val shared = project
  .in(file("shared"))
  .settings(Settings.common: _*)
  .settings(
    name := "shared",
    libraryDependencies ++= Seq(
      ws,
      Dependencies.commonsIO.core,
      // Akka
      Dependencies.akka.stream,
      Dependencies.akka.slf4j,
      // anorm
      Dependencies.anorm.core,
      Dependencies.anorm.akkaStream, // For monitoring
      // enums utils
      Dependencies.enumeratum.core,
      Dependencies.enumeratum.play,
      // cats
      Dependencies.cats.core,
      // play json extensions
      Dependencies.playJsonExtension.core,
      // JWT
      Dependencies.jwtPlay.core,
      // Crypto (used for JWT and IBM MQ)
      Dependencies.bouncyCastle.provider,
      Dependencies.bouncyCastle.pkix,
      // Convert HTML to PDF
      Dependencies.openhtmltopdf.core,
      Dependencies.openhtmltopdf.slf4j,
      Dependencies.openhtmltopdf.pdfbox,
      Dependencies.openhtmltopdf.java2d,
      // cache
      ehcache, // We can switch to the caffeine implementation (which is now the default) when we upgrade to Play 2.7
      Dependencies.caffeine.core,
      // pac4j + saml extension
      Dependencies.pac4j.core,
      Dependencies.pac4j.saml,
      Dependencies.pac4j.play,
      // server-side html rendering
      Dependencies.scalatags.core,
      // Test dependencies
      Dependencies.scalatest.play  % Test,
      Dependencies.scalacheck.main % Test
    )
  )
  .enablePlugins(PlayScala, BuildInfoPlugin)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(macros)

lazy val oracleLibs = project
  .in(file("libs/oracle"))
  .settings(name := "libs-oracle")

lazy val hotfixCommon = project
  .in(file("db-hotfix/common"))
  .settings(Settings.common: _*)
  .settings(name := "hotfix-common")
  .dependsOn(
    shared % "compile->compile;test->test",
    oracleLibs
  )

// We keep older hotfixes in the repo as examples, but there is no need to build them
lazy val hotfix4 = project
  .in(file("db-hotfix/fix4"))
  .settings(Settings.common: _*)
  .settings(name := "hotfix-4")
  .settings(Settings.defaultAssemblyMergeStrategy)
  .dependsOn(hotfixCommon % "compile->compile;test->test")

lazy val migrationCommon = project
  .in(file("db-migration/common"))
  .settings(Settings.common: _*)
  .settings(
    name := "migration-common"
  )
  .dependsOn(api % "compile->compile;test->test")

// We keep older migrations in the repo as examples, but there is no need to build them
lazy val migrationV270ToV280 = project
  .in(file("db-migration/V270ToV280"))
  .settings(Settings.common: _*)
  .settings(
    name := "migration-V270ToV280",
    dependencyOverrides ++= Seq(
      Dependencies.guava.core
    )
  )
  .settings(Settings.defaultAssemblyMergeStrategy)
  .dependsOn(migrationCommon % "compile->compile;test->test")

lazy val dbExporter = project
  .in(file("db-exporter"))
  .settings(Settings.common: _*)
  .settings(
    name := "db-exporter",
    version := "1.0.0",
    libraryDependencies ++= Seq(
      Dependencies.anorm.core,
      Dependencies.scopt.core,
      Dependencies.slf4j.core,
      Dependencies.slf4j.logback,
      Dependencies.slf4j.log4s,
      Dependencies.enumeratum.core,
      Dependencies.playJson.core,
      Dependencies.scalatest.core % Test
    )
  )
  .settings(Settings.defaultAssemblyMergeStrategy)
  .dependsOn(oracleLibs)

// release process
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  runClean,
  runTest,
  checkSnapshotDependencies,
  inquireVersions,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
