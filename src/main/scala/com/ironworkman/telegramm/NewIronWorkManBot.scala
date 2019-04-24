package com.ironworkman.telegramm

import cats.effect.{IO, Timer}
import cats.implicits._
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.io.StdIn
import scala.language.postfixOps

object NewIronWorkManBot extends TelegramBot with Polling {

  implicit val timer = IO.timer(ExecutionContext.global)
  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"
  val readLnIO: IO[String] = IO(StdIn.readLine()) //TODO : make async !!!
  def sendMessageIO(chatID: Long, msg: String): IO[Unit] =
    IO(request(SendMessage(chatID, msg)))
  def cleanAndToLong(input: String): Long = input.replace(" ", "").toLong

  def elapsed(number: Long = 0,
              duration: Long,
              amount: Long,
              chatId: Long): IO[(Long, Long, Long)] =
    for {
      _ <- sendMessageIO(chatId, s"The $number interval started. Work!")
      _ <- Timer[IO].sleep(duration second)
      _ <- sendMessageIO(chatId, s"The $number interval finished. Whats done?")
      _ <- sendMessageIO(
        chatId,
        s"Print minutes time for 1,2,3 categories separated by ;")
      input <- readLnIO
      acc <- input match {
        case "stop" =>
          sendMessageIO(chatId, "Go back and work, bitch!") *>
            IO((0L, 0L, 0L))
        case _ if number == amount =>
          sendMessageIO(chatId, "This is a statistic! ") *>
            IO((0L, 0L, 0L))
        case _ if number < amount =>
          elapsed(number + 1, duration, amount, chatId)
      }
      inputs <- IO(input.split(";"))
      acc <- IO(
        (acc._1 + cleanAndToLong(inputs(0)),
         acc._2 + cleanAndToLong(inputs(1)),
         acc._3 + cleanAndToLong(inputs(2))))
    } yield acc

  def program(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- elapsed(1, 15, 2, chatID)
        .flatMap(x => sendMessageIO(chatID, x.toString))
      _ <- Timer[IO].sleep(80 second)
    } yield ()

  val amount = 2
  val count = 0
  val acc = 0

//  override def onMessage(message: Message) = message.text match {
//    case Some(text) if text == "start" => program(text, message.chat.id).unsafeRunSync
//    case Some(text) if text == "state" => program(text, message.chat.id).unsafeRunSync
//    case Some(text) => sendMessageIO(message.chat.id, text)
//  }

  def main(args: Array[String]): Unit = NewIronWorkManBot.run()
}
