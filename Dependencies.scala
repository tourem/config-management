import sbt._

object Dependencies {
  object akka {
    val version = "2.5.23"
    val actor   = "com.typesafe.akka" %% "akka-actor" % version
    val stream  = "com.typesafe.akka" %% "akka-stream" % version
    val slf4j   = "com.typesafe.akka" %% "akka-slf4j" % version
  }

  object alpakka {
    val version = "1.1.0"

    val csv = "com.lightbend.akka" %% "akka-stream-alpakka-csv" % version
    val jms = "com.lightbend.akka" %% "akka-stream-alpakka-jms" % version
  }

  object anorm {
    val version = "2.5.3"

    val core       = "com.typesafe.play" %% "anorm"      % version
    val akkaStream = "com.typesafe.play" %% "anorm-akka" % version
  }

  object enumeratum {
    val core = "com.beachape" %% "enumeratum"      % "1.5.13"
    val play = "com.beachape" %% "enumeratum-play" % "1.5.15"
  }

  object scalatest {
    val version = "3.0.8"

    val core = "org.scalatest"          %% "scalatest"          % version
    val play = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"
  }

  object scalacheck {
    val version = "1.14.0"

    val main = "org.scalacheck" %% "scalacheck" % version
  }

  object cats {
    val version = "1.6.1"

    val core = "org.typelevel" %% "cats-core" % version
  }

  object commonsIO {
    val version = "2.6"

    val core = "commons-io" % "commons-io" % version
  }

  object playMailer {
    val version = "6.0.1"

    // We must provide an explicit version of 'javax.activation' instead of relying on the transitive dependency
    // to avoid a version conflict with another dependency relying on 'javax.activation-api'. The conflict cannot be
    // automatically resolved because tha artifact org and id have changed (thanks for that, Oracle)...
    val core              = "com.typesafe.play"  %% "play-mailer"     % version exclude ("javax.activation", "activation")
    val javaActivationFwk = "com.sun.activation" % "javax.activation" % "1.2.0"
  }

  object jsMessages {
    val version = "3.0.0"

    val core = "org.julienrf" %% "play-jsmessages" % version
  }

  object jwtPlay {
    val version = "1.1.0"

    val core = "com.pauldijou" %% "jwt-play" % version
  }

  object bouncyCastle {
    val version = "1.62"

    val provider = "org.bouncycastle" % "bcprov-jdk15on" % version
    val pkix     = "org.bouncycastle" % "bcpkix-jdk15on" % version
  }

  object openhtmltopdf {
    val version = "0.0.1-RC20"

    val core   = "com.openhtmltopdf" % "openhtmltopdf-core"   % version
    val slf4j  = "com.openhtmltopdf" % "openhtmltopdf-slf4j"  % version
    val pdfbox = "com.openhtmltopdf" % "openhtmltopdf-pdfbox" % version
    val java2d = "com.openhtmltopdf" % "openhtmltopdf-java2d" % version
  }

  object scrimage {
    val version = "2.1.8"

    val core    = "com.sksamuel.scrimage" % "scrimage-core_2.12"     % version
    val ioExtra = "com.sksamuel.scrimage" % "scrimage-io-extra_2.12" % version
  }

  object scalatags {
    val version = "0.7.0"

    val core = "com.lihaoyi" %% "scalatags" % version
  }

  object scalaLang {
    def scalaReflect(scalaVersion: String) = "org.scala-lang" % "scala-reflect" % scalaVersion
  }

  object pac4j {
    val pac4jVersion     = "2.3.1"
    val playPac4jVersion = "4.1.1"

    val core = "org.pac4j" % "pac4j"      % pac4jVersion
    val saml = "org.pac4j" % "pac4j-saml" % pac4jVersion
    val play = "org.pac4j" % "play-pac4j" % playPac4jVersion
  }

  object playJson {
    val version = "2.7.4"

    val core = "com.typesafe.play" %% "play-json" % version
  }

  object playJsonExtension {
    val version = "0.40.2"

    val core = "ai.x" %% "play-json-extensions" % version
  }

  object macWire {
    val version = "2.3.3"

    val macros = "com.softwaremill.macwire" %% "macros" % version
    val util   = "com.softwaremill.macwire" %% "util"   % version

  }

  object tagging {
    val version = "2.2.1"

    val core = "com.softwaremill.common" %% "tagging" % version
  }

  object scopt {
    val version = "3.7.1"

    val core = "com.github.scopt" %% "scopt" % version
  }

  object slf4j {
    val version = "1.7.27"

    val core    = "org.slf4j"      % "slf4j-api"       % version
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    val log4s   = "org.log4s"      %% "log4s"          % "1.8.2"
  }

  object silencer {
    val version = "1.4.2"

    val plugin = compilerPlugin("com.github.ghik" %% "silencer-plugin" % version)
    val lib    = "com.github.ghik" %% "silencer-lib" % version % Provided
  }

  object caffeine {
    val version = "2.8.0"

    val core = "com.github.ben-manes.caffeine" % "caffeine" % version
  }

  object guava {
    val version = "28.0-jre"

    val core = "com.google.guava" % "guava" % version
  }
}
