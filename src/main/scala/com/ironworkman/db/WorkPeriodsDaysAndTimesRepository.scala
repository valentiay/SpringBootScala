package com.ironworkman.db

import java.sql.Timestamp

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence._
import org.springframework.data.repository.CrudRepository

import scala.annotation.meta.field
import scala.beans.BeanProperty

@Entity
@Table(name = "WORK_PERIODS_DAYS_AND_TIMES")
case class WorkPeriodsDaysAndTimes(@(Id@field)
//                                   @(GeneratedValue@field)
                                   @BeanProperty id: Long,

                                   @BeanProperty startDayTime: Timestamp,

                                   @BeanProperty endDayTime: Timestamp,

                                   @BeanProperty periodMin: Long,

                                   @(ManyToOne@field)
                                   @(JoinColumn@field)(name = "usersId", nullable = true)
                                   @BeanProperty user: User
                                  ) {
  def this() {
    this(0, null, null, 0, null)
  }
}

trait WorkPeriodsDaysAndTimesRepository extends CrudRepository[WorkPeriodsDaysAndTimes, java.lang.Long] {}