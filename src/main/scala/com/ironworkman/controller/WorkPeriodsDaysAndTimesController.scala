package com.ironworkman.controller

import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

import cats.effect.{ExitCode, IO, IOApp, Timer}
import com.ironworkman.db.{User, WorkPeriodsDaysAndTimes, WorkPeriodsDaysAndTimesRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/intervals"))
class WorkPeriodsDaysAndTimesController(private val workPeriodsRepository: WorkPeriodsDaysAndTimesRepository) {
  @GetMapping(produces = Array("application/json"))
  def getAllPeriods() = workPeriodsRepository.findAll()

  @PostMapping(produces = Array("application/json"), consumes = Array("application/json"))
  def addPeriod(@RequestBody workPeriod: WorkPeriodsDaysAndTimes) =
    workPeriodsRepository.save(workPeriod)

  @PostMapping(produces = Array("text/plain"), consumes = Array("text/plain"))
  def addConfigureParam(@RequestBody inputString: String) = {

//    workPeriodsRepository.save(WorkPeriodsDaysAndTimes(2, 3, 60, User(1, "admin")))

    ConsoleScheduller().scheduler(0, 5, 3, 234234).unsafeRunAsyncAndForget()
  }

  @GetMapping(value = Array("/{amount}/{duration}"),
              produces = Array("application/json"))
  def startApplication(@PathVariable("amount") amount: Long,
                       @PathVariable("duration") duration: Long) = {

    // TODO: with IO

  }
}

case class ConsoleScheduller() {
  implicit val timer = IO.timer(ExecutionContext.global)

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
}
