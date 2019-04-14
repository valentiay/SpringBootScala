scalaVersion := "2.12.1"

organization := "com.ironworkman"
name := "SpringBootScala"
version := "1.0"
maintainer := "Vadim Opolski <vaopolskij@gmail.com>"
description := "Telegram bot"
organizationHomepage := Some(url("http://www.ironworkman.com"))

val catsVersion       = "1.6.0"
val catsEffectVersion = "1.1.0"

libraryDependencies ++= Seq(
  "org.projectlombok" % "lombok" % "1.16.20",
  "org.springframework.boot" % "spring-boot-starter" % "2.1.3.RELEASE",
  "org.springframework.boot" % "spring-boot-starter-web" % "2.1.3.RELEASE",
  "org.springframework.boot" % "spring-boot-starter-data-jpa" % "2.1.3.RELEASE",
  "org.liquibase" % "liquibase-core" % "3.6.3",
  "org.postgresql" % "postgresql" % "42.2.1",
  "org.springframework.boot" % "spring-boot-starter-test" % "2.1.3.RELEASE"

)

libraryDependencies += "org.typelevel" %% "cats-core"   % catsVersion
libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion

enablePlugins(JavaAppPackaging, AshScriptPlugin)

mainClass in Compile := Some("com.ironworkman.MyServiceApplication")

dockerBaseImage := "openjdk:8-jre-alpine"

dockerUpdateLatest := true