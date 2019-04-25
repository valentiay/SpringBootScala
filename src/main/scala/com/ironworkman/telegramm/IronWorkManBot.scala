package com.ironworkman.telegramm

import info.mukel.telegrambot4s._
import api._
import methods._
import cats.effect.{ExitCode, IO, Timer}

import scala.language.postfixOps
import cats.implicits._
import com.ironworkman.console.ConsoleScheduller.scheduler
import com.ironworkman.telegramm.IronWorkManBot.reply
import info.mukel.telegrambot4s.api.declarative.{Action, Commands}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

object IronWorkManBot extends TelegramBot with Polling with Commands {
  implicit val timer = IO.timer(ExecutionContext.global)

  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"

  def sendMessageIO(chatID: Long, msg: String): IO[Unit] = IO(request(SendMessage(chatID, msg)))

  def cleanAndToLong(input: String): Long = input.replace(" ", "").toLong

  def printlnIO(str: String) = IO(println(str))

  def scheduler(count: Long, amount: Long, duration: Long, chatId: Long): IO[Unit] =
    for {
      _ <- amount - count match {
            case 0 =>
              for {
                _ <- sendMessageIO(chatId, s"Your work is finished after $count intervals")
              } yield ()
            case _ =>
              for {
                _ <- sendMessageIO(chatId, s"The $count interval started, work!")
                _ <- Timer[IO].sleep(duration second)
                _ <- sendMessageIO(chatId, s"Write please whats done during the $count interval")
                _ <- Timer[IO].sleep(5 second)
                _ <- scheduler(count + 1, amount, duration, chatId)
              } yield ()
          }
    } yield ()

  def respond(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- sendMessageIO(chatID, msg)
    } yield ()

  onCommand("/hello") { implicit msg =>
    using(_.from) { user =>
      for {
        // TODO: Adding user to database
        _ <- reply(s"Hello ${user.firstName} ${user.lastName}! I`m IronWorkMan bot")
        _ <- reply(s"Enter please /start with intervals amount and duration")
        _ <- reply(s"/start 5 30")
        _ <- reply(s"This started sprint with 5 intervals of 30 minutes duration")
      } yield ExitCode.Success
    }
  }

  onCommand("/start") { implicit msg =>
    withArgs { args =>
      scheduler(0, args.head.toLong, args(1).toLong, msg.chat.id).unsafeRunAsyncAndForget()
      // TODO: saving to database
    }
  }

  def elapsed(count: Long = 0,
              intervalDuration: Long,
              intervalAmount: Long,
              chatId: Long,
              input: String): IO[(Long, Long, Long)] =
    for {
      acc <- input match {
              case _ if count == intervalAmount =>
                sendMessageIO(chatId, "This is a statistic! ") *>
                  IO(0L, 0L, 0L)
              case _ if count < intervalAmount => elapsed(count + 1, intervalDuration, intervalAmount, chatId, input)
            }
      inputs <- IO(input.split(";"))
      acc <- input.split(";") match {
              case Array(_, _, _) =>
                IO((acc._1 + cleanAndToLong(inputs(0)), acc._2 + cleanAndToLong(inputs(1)), acc._3 + cleanAndToLong(inputs(2))))
              case _ =>
                for {
                  _ <- sendMessageIO(chatId, s"The $count interval started. Work!")
                  _ <- Timer[IO].sleep(intervalDuration second)
                  _ <- sendMessageIO(chatId, s"The $count interval finished. Whats done?")
                  _ <- sendMessageIO(chatId, s"Print minutes time for 1,2,3 categories separated by ;")
                } yield acc
            }
    } yield acc

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}
