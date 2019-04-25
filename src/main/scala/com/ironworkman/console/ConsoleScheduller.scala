package com.ironworkman.console

import cats.effect.{ExitCode, IO, IOApp, Timer}

import scala.language.postfixOps
import scala.concurrent.duration._
import scala.language.postfixOps

object ConsoleScheduller extends IOApp {
  def printlnIO(str: String) = IO(println(str))

  def scheduler(count: Long, amount: Long, duration: Long, chatId: Long): IO[Unit] =
    for {
      _ <- amount - count match {
            case 0 =>
              for {
                _ <- printlnIO(s"Your work is finished after $count intervals")
              } yield ()
            case _ =>
              for {
                _ <- printlnIO(s"The $count interval started, work!")
                _ <- Timer[IO].sleep(duration second)
                _ <- printlnIO(s"Write please whats done during the $count interval")
                _ <- Timer[IO].sleep(5 second)
                _ <- scheduler(count + 1, amount, duration, chatId)
              } yield ()
          }
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- scheduler(0, 5, 3, 234234)
    } yield ExitCode.Success
}
