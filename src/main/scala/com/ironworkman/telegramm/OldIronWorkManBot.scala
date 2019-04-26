package com.ironworkman.telegramm

import com.ironworkman.db._
import info.mukel.telegrambot4s.models.Message

import scala.language.postfixOps

import info.mukel.telegrambot4s._
import api._
import methods._
import Implicits._
import cats.effect.{IO, Timer}

import scala.language.postfixOps

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

/*
   В данном случае лучше сделать обычный класс, а не case-класс, т.к.
    1) используется наследование, а для кейс-классов наследование - это плохая идея из-за автоматически заданных операторов
       equals и hashCode (https://stackoverflow.com/questions/11158929/what-is-so-wrong-with-case-class-inheritance).
       Здесь проблем может не быть, но все равно лучше так не делат.
    2) case-классы обычно используется для представления каких-то моделей данных. В этом случае как раз полезны equals и
       hashCode, но классы, которые используются для обработки данных, в этих операторах не нуждаются. К тому же, так
       легче отличать обработчики от данных.

   Кроме того, параметры у case-классов по-умолчанию val, поэтому здесь val перед каждой
   переменной в конструкторе не нужен.
 */
case class OldIronWorkManBot(val userRepository: UserRepository,
                             val workPeriodRepository: WorkPeriodRepository,
                             val workPeriodsDaysAndTimesRepository: WorkPeriodsDaysAndTimesRepository,
                             val categoryRepository: CategoryRepository)
    extends TelegramBot with Polling with Commands {

  /*
    Здесь, наверное, самый важный момент: использовать ExecutionContext.global опасно, потому что в нем явно не
    указывается число потоков в thread-пуле. Из-за этого сложно предсказать производительность.
    Особенно внимательно стоит относиться к блокирующим вызовам: каждый блокирующий вызов занимает один поток. Если
    будут заняты все потоки в пуле, новые вызовы будет невозможно начать, пока старые не закончатся.
    Сторонние библиотеки (обычно, не очень хорошего качества) тоже могут использовать ExecutionContext.global, что
    только усугубляет ситуацию.

    Более "красиво" это написано здесь:
    https://typelevel.org/cats-effect/concurrency/basics.html#blocking-threads
    https://monix.io/docs/2x/best-practices/blocking.html#if-blocking-use-scalas-blockcontext

    Про создание кастомных ExecutionContext лучше погуглить, это довольно широкая тема.

    Уделяю это такое внимание, потому что некоторые вызовы в коде, например
      IO(userRepository.save(User(userId, userName)))
    , выглядят блокирующими (Я могу в этом ошибаться). Лучше их избегать и использовать асинхронные драйверы к базам
    данных, но если таких нет, то нужно внимательно следить за ExecutionContext-ами.
   */
  implicit val timer                                     = IO.timer(ExecutionContext.global)
  def token                                              = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"
  def sendMessageIO(chatID: Long, msg: String): IO[Unit] = IO(request(SendMessage(chatID, msg)))
  def cleanAndToLong(input: String): Long                = input.replace(" ", "").toLong

  def greeting(chatId: Long, userId: Long, userName: String): IO[Unit] =
    for {
      _ <- respond(s"Hello ${userName}! I`m IronWorkMan bot.", chatId)
      _ <- respond(s"Please enter a command with the parameters of the number of " +
                     s"intervals and the duration of the interval in minutes.",
                   chatId)
      _ <- respond(s"For Example /start 5 30", chatId)
      _ <- respond(s"A sprint of 5 intervals of 30 minutes will start.", chatId)
      _ <- IO(userRepository.save(User(userId, userName)))
    } yield ()

  def startSprint(chatId: Long, userId: Long, userName: String, duration: Long, amount: Long): IO[Unit] =
    for {
      _ <- respond(s"A sprint of $amount intervals of $duration minutes started.", chatId)
      _ <- IO(
            workPeriodsDaysAndTimesRepository
              .save(WorkPeriodsDaysAndTimes(chatId, amount, duration, User(userId, userName))))
      _ <- scheduler(0, amount.toLong, duration.toLong, chatId)
    } yield ()

  def scheduler(count: Long, amount: Long, duration: Long, chatId: Long): IO[Unit] =
    for {
      _ <- amount - count match {
            case 0 =>
              for {
                _ <- respond(s"Your sprint is finished after $count intervals", chatId)
                _ <- IO(workPeriodRepository.findSumByCategoryIdAndUser2(1))
                      .flatMap(paid => respond(s"You were paid for $paid minutes", chatId))
                _ <- IO(workPeriodRepository.findSumByCategoryIdAndUser2(2))
                      .flatMap(dontStopPaying => respond(s"You did not stop paying for $dontStopPaying minutes", chatId))
                _ <- IO(workPeriodRepository.findSumByCategoryIdAndUser2(3))
                      .flatMap(dontPay => respond(s"You were not paid for $dontPay minutes", chatId))
              } yield ()
            case _ =>
              for {
                _ <- respond(s"The $count interval started, work!", chatId)
                _ <- Timer[IO].sleep(duration second)
                _ <- respond(s"Write please time for 1,2,3 categories of $count interval", chatId)
                _ <- respond(s"Example: /time 15 10 5 programming drink tea mail", chatId)
                _ <- Timer[IO].sleep(20 second)
                _ <- scheduler(count + 1, amount, duration, chatId)
              } yield ()
          }
    } yield ()

  def recordTime(chatId: Long, paidTime: Long, dontStopPayingTime: Long, notPayingTime: Long, description: String): IO[Unit] =
    for {
      workPeriodsDaysAndTimes <- IO(workPeriodsDaysAndTimesRepository.findById(chatId))
      paid                    <- IO(categoryRepository.findById(1L))
      _ <- IO(
            workPeriodRepository// См коммент в WorkPeriodRepository.scala
              .save(WorkPeriod(null, paidTime, description, paid.get, workPeriodsDaysAndTimes.get)))
      _              <- respond(s"A time is recorded", chatId)
      dontStopPaying <- IO(categoryRepository.findById(2L))
      _ <- IO(
            workPeriodRepository
              .save(WorkPeriod(null, dontStopPayingTime, description, dontStopPaying.get, workPeriodsDaysAndTimes.get)))
      _         <- respond(s"A time is recorded", chatId)
      notPaying <- IO(categoryRepository.findById(3L))
      _ <- IO(
            workPeriodRepository
              .save(WorkPeriod(null, notPayingTime, description, notPaying.get, workPeriodsDaysAndTimes.get)))
      _ <- respond(s"A time is recorded", chatId)
    } yield ()

  override def onMessage(message: Message) = message.text match {
    case Some(text) if text.contains("start") =>
      text.split(" ") match {
        case Array(_, amount, duration) =>
          startSprint(message.chat.id, message.from.get.id, message.from.get.username.get, amount.toLong, duration.toLong)
            .unsafeRunAsyncAndForget()
        case _ =>
          respond("Enter command /start and two parameters, please", message.chat.id)
            .unsafeRunAsyncAndForget()
      }
    case Some(text) if text.contains("time") =>
      text.split(" ") match {
        case Array(_, paidTime, dontStopPayingTime, notPayingTime, description) =>
          recordTime(message.chat.id, paidTime.toLong, dontStopPayingTime.toLong, notPayingTime.toLong, description)
            .unsafeRunAsyncAndForget()
        case _ =>
          respond("Enter command /time and four parameters, please", message.chat.id)
            .unsafeRunAsyncAndForget()
      }
    case Some(text) =>
      greeting(message.chat.id, message.from.get.id, message.from.get.username.get)
        .unsafeRunAsyncAndForget()
  }

  def respond(msg: String, chatID: Long): IO[Unit] =
    // Это выражение аналогично простому sendMessageIO(chatID, msg)
    // (Возможно заделка на будущее, в новой версии выглядит по-другому)
    for {
      _ <- sendMessageIO(chatID, msg)
    } yield ()

}
