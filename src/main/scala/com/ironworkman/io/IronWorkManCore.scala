package com.ironworkman.io

import cats.effect.{ExitCode, IO, IOApp, Timer}

import scala.concurrent.duration._
import scala.io.StdIn
import scala.language.postfixOps
import cats.implicits._

object IronWorkManCore extends IOApp {
  def printlnIO(str: String): IO[Unit] = IO(println(str))

  def printlnIO(str: (Long, Long, Long)): IO[Unit] = IO(println(str))

  val readLnIO: IO[String] = IO(StdIn.readLine()) //TODO : make async !!!

  def elapsed(number: Long = 0, duration: Long, amount: Long): IO[(Long, Long, Long)] =
    for {
      _ <- printlnIO(s"The $number interval started. Work!")
      _ <- Timer[IO].sleep(duration second)
      _ <- printlnIO(s"The $number interval finished. Whats done?")
      _ <- printlnIO(s"Print minutes time for 1,2,3 categories separated by :")
      input <- readLnIO
      acc <- input match {
        case "stop" => printlnIO("Go back and work, bitch!") *>
          IO((0L, 0L, 0L))
        case _ if number == amount => printlnIO("This is a statistic! ") *>
          IO((0L, 0L, 0L))
        case _ if number < amount => elapsed(number + 1, duration, amount)
      }
      inputs <- IO(input.split(";"))
      acc <- IO((acc._1 + inputs(0).toLong, acc._2 + inputs(1).toLong, acc._3 + inputs(2).toLong))
    } yield acc

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- printlnIO("Hello write pls amount of intervals")
      amount <- readLnIO
      _ <- printlnIO("Write pls interval duration")
      duration <- readLnIO
      _ <- (Timer[IO].sleep(1 millisecond) *>
        elapsed(1, duration.toLong, amount.toLong).flatMap(x => printlnIO(x))).start
      _ <- Timer[IO].sleep(duration.toLong * amount.toLong * 10 second)
    } yield ExitCode.Success
}