package com.ironworkman.telegramm

import info.mukel.telegrambot4s._
import api._
import methods._
import models._
import Implicits._
import cats.effect.{IO, Timer}

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

  def elapsed(count: Long = 0, duration: Long, intervalAmount: Long, chatId: Long, input: String): IO[(Long, Long, Long)] =
    for {
      acc <- input match {
        case _ if count == intervalAmount => sendMessageIO(chatId, "This is a statistic! ") *>
          IO(0L, 0L, 0L)
        case _ if  count < intervalAmount => elapsed(count + 1, duration, intervalAmount, chatId, input)
      }
      inputs <- IO(input.split(";"))
      acc <- input.split(";") match {
        case Array(_,_,_) =>  IO ((acc._1 + cleanAndToLong (inputs (0)),
          acc._2 + cleanAndToLong (inputs (1)),
          acc._3 + cleanAndToLong (inputs (2))))
        case _ => for {
          _ <- sendMessageIO(chatId, s"The $count interval started. Work!")
          _ <- Timer[IO].sleep(duration second)
          _ <- sendMessageIO(chatId, s"The $count interval finished. Whats done?")
          _ <- sendMessageIO(chatId, s"Print minutes time for 1,2,3 categories separated by ;")
        } yield acc
      }
    } yield acc

  def scheduler(count:Long, seconds :Long,  chatId: Long): IO[Unit] =
    for {
      _ <- seconds-count match {
        case 0 => for {
          _ <- sendMessageIO(chatId, s"First interval is finished after $count seconds")
        } yield()
        case _ => for {
          _ <- Timer[IO].sleep(1 second)
          _ <- sendMessageIO(chatId, s"next $count seconds")
          _ <- scheduler(count+1, seconds, chatId)
        } yield()
      }
    } yield ()

  def respond(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- sendMessageIO(chatID, msg)
    } yield ()


  override def onMessage(message: Message) = message.text match {
    case Some(text) if text == "start"  => respond("Please enter 3 intervals separated by ;", message.chat.id).unsafeRunAsyncAndForget()
    case Some(text)  =>  scheduler(0, 5, message.chat.id).unsafeRunAsyncAndForget()
  }

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}