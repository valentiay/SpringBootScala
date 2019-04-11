package de.codecentric.microservice.controller

import de.codecentric.microservice.db.{CategoryRepository, User, WorkPeriodsDaysAndTimes, WorkPeriodsDaysAndTimesRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/intervals"))
class WorkPeriodsDaysAndTimesController(private val workPeriodsRepository: WorkPeriodsDaysAndTimesRepository) {
  @GetMapping(produces = Array("application/json"))
  def getAllPeriods() = workPeriodsRepository.findAll()

  @PostMapping(produces = Array("application/json"), consumes = Array("application/json"))
  def addPeriod(@RequestBody workPeriod: WorkPeriodsDaysAndTimes) =
    workPeriodsRepository.save(workPeriod)

  @PostMapping(produces = Array("application/json"), consumes = Array("application/json"))
  def addPeriodFromString(@RequestBody workPeriod: WorkPeriodsDaysAndTimes) =
    workPeriodsRepository.save(workPeriod)


}