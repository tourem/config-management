// The Typesafe repository
resolvers += "Typesafe repository" at "https://crm-nexus.itg.echonet/repository/public/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.23")

// To keep an homogeneous code style
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.2.1")

// Add build infos into code
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// plugin to manage release cycle
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")

// plugin to package app
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

// Fingerprinting front assets to avoid browser's caching after a release (only in prod mode)
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
addSbtPlugin("com.github.mwz" % "sbt-sonar" % "2.1.0")

