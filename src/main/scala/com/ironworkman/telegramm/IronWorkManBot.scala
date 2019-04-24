package com.ironworkman.telegramm

import java.sql.Timestamp

import info.mukel.telegrambot4s._
import api._
import methods._
import models._
import Implicits._
import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp, Timer}
import com.ironworkman.db.{
  WorkPeriodRepository,
  WorkPeriodsDaysAndTimes,
  WorkPeriodsDaysAndTimesRepository
}
import com.ironworkman.db.{User, UserRepository}
import info.mukel.telegrambot4s.api.declarative.{Action, Commands, ToCommand}

import scala.io.StdIn
import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class IronWorkManBot(
    val userRepository: UserRepository,
    val workPeriodRepository: WorkPeriodRepository,
    val workPeriodsDaysAndTimesRepository: WorkPeriodsDaysAndTimesRepository)
    extends TelegramBot
    with Polling
    with Commands {

  implicit val timer = IO.timer(ExecutionContext.global)

  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"

  def sendMessageIO(chatID: Long, msg: String): IO[Unit] =
    IO(request(SendMessage(chatID, msg)))

  def cleanAndToLong(input: String): Long = input.replace(" ", "").toLong

  def elapsed(count: Long = 0,
              duration: Long,
              amount: Long,
              chatId: Long,
              input: String): IO[Unit] =
    for {
      inputs <- IO(input.split(";"))
      _ <- inputs match {
        case Array(_, _, _) =>
          IO(
            (cleanAndToLong(inputs(0)),
             cleanAndToLong(inputs(1)),
             cleanAndToLong(inputs(2))))
        case _ =>
          for {
            _ <- sendMessageIO(chatId, s"The $count interval started. Work!")
            _ <- Timer[IO].sleep(duration second)
            _ <- sendMessageIO(chatId,
                               s"The $count interval finished. Whats done?")
            _ <- sendMessageIO(
              chatId,
              s"Print for $count  1,2,3 categories separated by ;")
            _ <- Timer[IO].sleep(duration second)
          } yield ()
      }
    } yield ()

  def startProgram(username: String,
                   userId: Long,
                   msg: String,
                   chatId: Long,
                   duration: Long,
                   amount: Long,
                   count: Long): IO[Unit] =
    for {
//      user <- userRepository.findById(userId).get match {
//        case User(_, _) => User(userId, username)
//        case _ => userRepository.save(User(userId, username))
//      }
      inputs <- IO(msg.split(";"))
      _ <- inputs match {
        case Array(_, duration, amount) =>
          for {
            _ <- IO(userRepository.save(User(userId, username)))
            _ <- IO(
              workPeriodsDaysAndTimesRepository
                .save(
                  WorkPeriodsDaysAndTimes(
                    chatId.toLong,
                    new Timestamp(System.currentTimeMillis),
                    new Timestamp(System.currentTimeMillis),
                    cleanAndToLong(duration),
                    User(userId, username))))
          } yield ()
        case _ =>
          for {
            _ <- sendMessageIO(chatId, s"The $count interval started. Work!")
            _ <- Timer[IO].sleep(duration second)
            _ <- sendMessageIO(chatId,
                               s"The $count interval finished. Whats done?")
            _ <- sendMessageIO(
              chatId,
              s"Print for $count  1,2,3 categories separated by ;")
          } yield ()
      }
      _ <- Timer[IO].sleep(3000 second)
    } yield ()

  def finishProgram(msg: String, chatID: Long): IO[Unit] =
    for {
      _ <- sendMessageIO(chatID, msg)
    } yield ()

//  override def onMessage(message: Message) = message.text match {
//    case Some(text) if text.contains("start") => startProgram(message.from.get.username.get, message.from.get.id,
//      message.text.get, message.chat.id, 15, 2, 0).unsafeRunAsyncAndForget()
//    case Some(text) if text == "stop" => finishProgram("See you later", message.chat.id)
//    case Some(text) => elapsed(1, 15, 2, message.chat.id, text)
//    case _ => println("No text")
//  }

  val rng = new scala.util.Random(System.currentTimeMillis())
  onCommand("/die") { implicit msg =>
    reply((rng.nextInt(6) + 1).toString)
  }
}
