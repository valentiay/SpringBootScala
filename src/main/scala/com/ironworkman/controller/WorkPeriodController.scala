package com.ironworkman.controller

import com.ironworkman.db.{Category, WorkPeriod, WorkPeriodRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/period"))
class WorkPeriodController(private val workPeriodRepository: WorkPeriodRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllWorkIntervals() = workPeriodRepository.findAll()

  @PostMapping(produces = Array("application/json"), consumes = Array("application/json"))
  def addIntervals(@RequestBody workPeriod: WorkPeriod) =
    workPeriodRepository.save(workPeriod)

  @PostMapping(produces = Array("text/plain"), consumes = Array("text/plain"))
  def addIntervalsFromString(@RequestBody inputString: String) = {
    val category: Category = Category("Test", "Test")
    // TODO: scheduller
    //    val workPeriod: WorkPeriod = WorkPeriod()
    //    workPeriodRepository.save(workPeriod)

    println("Test Caontainer")
  }
}
