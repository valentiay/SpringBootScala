package com.ironworkman.telegramm

import info.mukel.telegrambot4s._
import api._
import methods._
import cats.effect.{ExitCode, IO, Timer}

import scala.language.postfixOps
import cats.implicits._
import info.mukel.telegrambot4s.api.declarative.Commands

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

object IronWorkManBot extends TelegramBot with Polling with Commands {
  implicit val timer = IO.timer(ExecutionContext.global)
  // TODO: To properties
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
                // TODO: Reading from database statistics
              } yield ()
            case _ =>
              for {
                _ <- sendMessageIO(chatId, s"The $count interval started, work!")
                _ <- Timer[IO].sleep(duration second)
                _ <- sendMessageIO(chatId, s"Write please time for 1,2,3 categories of $count interval")
                _ <- sendMessageIO(chatId, s"Example: /time 15 10 5 programming drink tea mail")
                _ <- Timer[IO].sleep(15 second)
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
        _ <- reply(s"This started sprint with 5 intervals 30 minutes even")
      } yield ExitCode.Success
    }
  }

  onCommand("/start") { implicit msg =>
    withArgs { args =>
      scheduler(0, args.head.toLong, args(1).toLong, msg.chat.id).unsafeRunAsyncAndForget()
    // TODO: saving to database
    }
  }

  onCommand("/time") { implicit msg =>
    withArgs { args =>
      for {
        _ <- reply(s"You spend ${args.head.toLong} minutes in paid category")
        _ <- reply(s"You spend ${args(1)} minutes in dont stop paid category")
        _ <- reply(s"You spend ${args(2)} minutes in not pay category")
      // TODO: saving to database
      } yield ExitCode.Success
    }
  }

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}
