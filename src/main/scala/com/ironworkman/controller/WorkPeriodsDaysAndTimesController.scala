package com.ironworkman.controller

import java.sql.Timestamp

import com.ironworkman.db.{
  User,
  WorkPeriodsDaysAndTimes,
  WorkPeriodsDaysAndTimesRepository
}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/intervals"))
class WorkPeriodsDaysAndTimesController(
    private val workPeriodsRepository: WorkPeriodsDaysAndTimesRepository) {
  @GetMapping(produces = Array("application/json"))
  def getAllPeriods() = workPeriodsRepository.findAll()

  @PostMapping(produces = Array("application/json"),
               consumes = Array("application/json"))
  def addPeriod(@RequestBody workPeriod: WorkPeriodsDaysAndTimes) =
    workPeriodsRepository.save(workPeriod)

  @PostMapping(produces = Array("text/plain"), consumes = Array("text/plain"))
  def addPeriodFromString(@RequestBody inputString: String) = {

    val workPeriodsDaysAndTimes: WorkPeriodsDaysAndTimes = WorkPeriodsDaysAndTimes(0,  4, 30, User(1, "admin"))

    workPeriodsRepository.save(workPeriodsDaysAndTimes)
  }

//  @PostMapping(produces = Array("text/plain"), consumes = Array("text/plain"))
//  def addConfigureParam(@RequestBody inputString: String) = {
//    val timestamp: Timestamp = new Timestamp(System.currentTimeMillis())
//    val timestamp2: Timestamp = new Timestamp(System.currentTimeMillis() + 1000000000)
//
//    val workPeriodsDaysAndTimes: WorkPeriodsDaysAndTimes
//          = WorkPeriodsDaysAndTimes(0, timestamp, timestamp2, 60, User(1, "admin"))
//
//    workPeriodsRepository.save(workPeriodsDaysAndTimes)
//  }

  @GetMapping(value = Array("/{amount}/{duration}"),
              produces = Array("application/json"))
  def startApplication(@PathVariable("amount") amount: Long,
                       @PathVariable("duration") duration: Long) = {

    println("Test")

  }
}
