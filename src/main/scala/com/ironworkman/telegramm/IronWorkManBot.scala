package com.ironworkman.telegramm

import info.mukel.telegrambot4s._
import api._
import methods._
import models._
import Implicits._
import cats.effect.{ExitCode, IO, Timer}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

object IronWorkManBot extends TelegramBot with Polling with Commands {

  implicit val timer = IO.timer(ExecutionContext.global)
  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"
  def printlnIO(str: String): IO[Unit] = IO(println(str))

  def program(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- printlnIO(msg)
      _ <- IO(request(SendMessage(chatID, msg)))
      _ <- Timer[IO].sleep(5 second)
      _ <- IO(request(SendMessage(chatID, msg)))
    } yield ()


  override def onMessage(message: Message) = message.text match {
    case Some(text) => program(text, message.chat.id).unsafeRunSync
    case _ => {
      println("No methods")
    }
  }

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}