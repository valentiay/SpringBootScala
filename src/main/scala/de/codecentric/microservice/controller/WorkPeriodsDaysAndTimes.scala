package de.codecentric.microservice.controller

import de.codecentric.microservice.db.{CategoryRepository, WorkPeriodsDaysAndTimesRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/intervals"))
class WorkPeriodsDaysAndTimes(private val workPeriodsDaysAndTimes: WorkPeriodsDaysAndTimesRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllCategory() = workPeriodsDaysAndTimes.findAll()
}