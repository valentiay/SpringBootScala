package de.codecentric.microservice.controller

import de.codecentric.microservice.db.{WorkPeriodRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/period"))
class WorkPeriodController(private val workPeriodRepository: WorkPeriodRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllWorkPeriod() = workPeriodRepository.findAll()
}