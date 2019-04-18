scalaVersion := "2.12.1"

organization := "com.ironworkman"
name := "SpringBootScala"
version := "1.0"
maintainer := "Vadim Opolski <vaopolskij@gmail.com>"
description := "Telegram bot"
organizationHomepage := Some(url("http://www.ironworkman.com"))

val catsVersion = "1.6.0"
val catsEffectVersion = "1.1.0"
val springBootVersion = "2.1.3.RELEASE"
val lombokVersion = "1.16.20"
val liqibaseVersion = "3.6.3"
val PgSQLVersion = "42.2.1"
val telegrammBotVersion = "2.9.3"

libraryDependencies ++= Seq(
  "org.projectlombok" % "lombok" % lombokVersion,
  "org.springframework.boot" % "spring-boot-starter" % springBootVersion,
  "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion,
  "org.springframework.boot" % "spring-boot-starter-data-jpa" % springBootVersion,
  "org.liquibase" % "liquibase-core" % liqibaseVersion,
  "org.postgresql" % "postgresql" % PgSQLVersion,
  "org.springframework.boot" % "spring-boot-starter-test" % springBootVersion,
  "info.mukel" %% "telegrambot4s" % telegrammBotVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion
)

enablePlugins(JavaAppPackaging, AshScriptPlugin)

mainClass in Compile := Some("com.ironworkman.MyServiceApplication")

dockerBaseImage := "openjdk:8-jre-alpine"

dockerUpdateLatest := true