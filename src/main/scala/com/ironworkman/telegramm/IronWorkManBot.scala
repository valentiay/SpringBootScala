package com.ironworkman.telegramm

import info.mukel.telegrambot4s._
import api._
import methods._
import models._
import Implicits._
import cats.effect.{ExitCode, IO, IOApp, Timer}

import scala.concurrent.duration._
import scala.io.StdIn
import scala.language.postfixOps
import cats.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

object IronWorkManBot extends TelegramBot with Polling with Commands {

  implicit val timer = IO.timer(ExecutionContext.global)
  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"
  val readLnIO: IO[String] = IO(StdIn.readLine()) //TODO : make async !!!
  def sendMessageIO(chatID: Long, msg:String): IO[Unit] = IO(request(SendMessage(chatID, msg)))
  def cleanAndToLong(input: String): Long = input.replace(" ", "").toLong

  def elapsed(number: Long = 0, duration: Long, amount: Long, chatId: Long): IO[(Long, Long, Long)] =
    for {
      _ <- sendMessageIO(chatId, s"The $number interval started. Work!")
      _ <- Timer[IO].sleep(duration second)
      _ <- sendMessageIO(chatId, s"The $number interval finished. Whats done?")
      _ <- sendMessageIO(chatId, s"Print minutes time for 1,2,3 categories separated by ;")
      input <- readLnIO
      acc <- input match {
        case "stop" => sendMessageIO(chatId, "Go back and work, bitch!") *>
          IO((0L, 0L, 0L))
        case _ if number == amount => sendMessageIO(chatId, "This is a statistic! ") *>
          IO((0L, 0L, 0L))
        case _ if number < amount => elapsed(number + 1, duration, amount, chatId)
      }
      inputs <- IO(input.split(";"))
      acc <- IO((acc._1 + cleanAndToLong(inputs(0)),
        acc._2 + cleanAndToLong(inputs(1)),
        acc._3 + cleanAndToLong(inputs(2)))
      )
    } yield acc

  def program(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- sendMessageIO(chatID, "Привет, начнем")
      _ <- Timer[IO].sleep(1 millisecond) *>
        elapsed(1, 15, 2, chatID).flatMap(x => sendMessageIO(chatID, x.toString()))
      _ <- Timer[IO].sleep(80 second)
    } yield ()


  override def onMessage(message: Message) = message.text match {
    case Some(text) if text == "start" => program(text, message.chat.id).unsafeRunSync
    case Some(text) => sendMessageIO(message.chat.id, text)
    case _ => {println("No methods")}
  }

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}