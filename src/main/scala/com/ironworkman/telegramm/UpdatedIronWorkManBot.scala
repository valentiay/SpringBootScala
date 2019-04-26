package com.ironworkman.telegramm

import java.sql.Timestamp

import cats.effect.{ExitCode, IO, Timer}
import com.ironworkman.db._
//import com.ironworkman.telegramm.IronWorkManBot.{cleanAndToLong, onCommand, reply, sendMessageIO, using}
//import info.mukel.telegrambot4s.api._
//import info.mukel.telegrambot4s.api.declarative.{Action, Commands}
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models.Message

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps


class UpdatedIronWorkManBot(
                      val userRepository: UserRepository,
                      val workPeriodRepository: WorkPeriodRepository,
                      val workPeriodsDaysAndTimesRepository: WorkPeriodsDaysAndTimesRepository)
//  extends TelegramBot
//    with Polling
//    with Message
{
//  implicit val timer = IO.timer(ExecutionContext.global)
//  // TODO: To properties
//  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"
//
//  def MessageIO(chatID: Long, msg: String): IO[Unit] = IO(request(SendMessage(chatID, msg)))
//
//  def cleanAndToLong(input: String): Long = input.replace(" ", "").toLong
//
//  def printlnIO(str: String) = IO(println(str))
//
//  def scheduler(count: Long, amount: Long, duration: Long, chatId: Long): IO[Unit] =
//    for {
//      _ <- amount - count match {
//            case 0 =>
//              for {
//                _ <- MessageIO(chatId, s"Your work is finished after $count intervals")
//                // TODO: Reading from database statistics
//              } yield ()
//            case _ =>
//              for {
//                _ <- MessageIO(chatId, s"The $count interval started, work!")
//                _ <- Timer[IO].sleep(duration second)
//                _ <- MessageIO(chatId, s"Write please time for 1,2,3 categories of $count interval")
//                _ <- MessageIO(chatId, s"Example: /time 15 10 5 programming drink tea mail")
//                _ <- Timer[IO].sleep(15 second)
//                _ <- scheduler(count + 1, amount, duration, chatId)
//              } yield ()
//          }
//    } yield ()
//
//  def respond(msg: String, chatID: Long): IO[Unit] =
//    for {
//      _ <- MessageIO(chatID, msg)
//    } yield ()
//
////  onCommand("/hello") { implicit msg =>
////    for {
////      _ <- reply(s"Hello ${msg.from.get.firstName} ${msg.from.get.lastName}! I`m IronWorkMan bot")
////      _ <- reply(s"Enter please /start with intervals amount and duration")
////      _ <- reply(s"/start 5 30")
////      _ <- reply(s"This started sprint with 5 intervals 30 minutes even")
////      //TODO: Adding user to database
////      _ <- IO(userRepository.save(User(msg.from.get.id, msg.from.get.username.get)))
////    } yield ()
////  }
//
//
//
////  def greeting(user: User): IO[Unit] =
////    for {
////      _ <- MessageIO(s"Hello ${user.firstName} ${msg.from.get.lastName}! I`m IronWorkMan bot")
////      _ <- reply(s"Enter please /start with intervals amount and duration")
////      _ <- reply(s"/start 5 30")
////      _ <- reply(s"This started sprint with 5 intervals 30 minutes even")
////      //TODO: Adding user to database
////      _ <- IO(userRepository.save(User(msg.from.get.id, msg.from.get.username.get)))
////    } yield yield ()
//
//
////  def startProgram(username: String, userId: Long, msg: String, chatId: Long, duration: Long, amount: Long, count: Long): IO[Unit] =
////    for {
////      inputs <- IO(msg.split(";"))
////      _ <- inputs match {
////        case Array(_, duration, amount) => for {
////          _ <- IO(userRepository.save(User(userId, username)))
////          _ <- IO(workPeriodsDaysAndTimesRepository
////            .save(WorkPeriodsDaysAndTimes(chatId.toLong,
////              new Timestamp(System.currentTimeMillis),
////              new Timestamp(System.currentTimeMillis),
////              cleanAndToLong(duration), User(userId, username))))
////        } yield ()
////        case _ => for {
////          _ <- MessageIO(chatId, s"The $count interval started. Work!")
////          _ <- Timer[IO].sleep(duration second)
////          _ <- MessageIO(chatId, s"The $count interval finished. Whats done?")
////          _ <- MessageIO(chatId, s"Print for $count  1,2,3 categories separated by ;")
////          _ <- MessageIO(chatId, s"Print for $count  1,2,3 categories separated by ;")
////        } yield ()
////      }
////      _ <- Timer[IO].sleep(3000 second)
////    } yield ()
//
////  override def onMessage(message: Message) = message.text match {
////    case Some(text) if text.contains("start") =>
////      startProgram(message.from.get.username.get, message.from.get.id, message.text.get, message.chat.id, 15, 2, 0)
////        .unsafeRunAsyncAndForget()
////    case Some(text) if text.contains("time") => recordTime("See you later", message.chat.id)
////    case Some(text)                          => greeting(message.from.get)
////    case _                                   => println("No text")
////  }
//
//  onCommand("/start") { implicit msg =>
//    withArgs { args =>
//      scheduler(0, args.head.toLong, args(1).toLong, msg.chat.id).unsafeRunAsyncAndForget()
//    // TODO: saving to database
//    }
//  }
//
//  onCommand("/time") { implicit msg =>
//    withArgs { args =>
//      for {
//        _ <- reply(s"You spend ${args.head.toLong} minutes in paid category")
//        _ <- reply(s"You spend ${args(1)} minutes in dont stop paid category")
//        _ <- reply(s"You spend ${args(2)} minutes in not pay category")
//      // TODO: saving to database
//      } yield ExitCode.Success
//    }
//  }
//
////  onCommand("/hello") { implicit msg =>
////    for {
////      _ <- IO.fromFuture(IO.pure(reply(s"Hello ${msg.from.get.firstName}")))
////      urIO <- IO(userRepository)
////        /* Лучше не делать вызовы unsafeRunAsyncAndForget внутри flatMap-ов (== for-comprehension).
////           Нужно строить архитектуру так, чтобы unsafeRunAsyncAndForget вызывался один раз в самом конце
////           цепочки flatMap-ов
////        */
////      _ <- IO(IO(urIO.save(User(msg.from.get.id, msg.from.get.username.get))).unsafeRunAsyncAndForget())
////    } yield ()
////  }
//
//
//
////  onCommand("/hello") { implicit msg =>
////    using(_.from) { user =>
////      for {
////        // TODO: Adding user to database
////        _ <- reply(s"Hello ${user.firstName} ${user.lastName}! I`m IronWorkMan bot")
////        _ <- reply(s"Enter please /start with intervals amount and duration")
////        _ <- reply(s"/start 5 30")
////        _ <- reply(s"This started sprint with 5 intervals 30 minutes even")
////      } yield ExitCode.Success
////    }
////  }
//
//

}