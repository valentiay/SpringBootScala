package com.ironworkman.db

import javax.persistence._
import org.springframework.data.jpa.repository.JpaRepository

import scala.annotation.meta.field
import scala.beans.BeanProperty
@Entity
@Table(name = "WORK_PERIOD")
case class WorkPeriod(
                       @(Id@field)
                       @(GeneratedValue@field)
                        @BeanProperty id: java.lang.Long,

                        @BeanProperty timeAmount: Long,

                        @BeanProperty description: String,

                        @(ManyToOne @field)
                        @(JoinColumn @field)(name = "categoriesId", nullable = true)
                        @BeanProperty category: Category,

                        @(ManyToOne @field)
                        @(JoinColumn @field)(name = "workPeriodsDaysAndTimesId", nullable = true)
                        @BeanProperty workPeriodsDaysAndTimes: WorkPeriodsDaysAndTimes) {

  def this() {
    this(null, 0, "", null, null)
  }

}

trait WorkPeriodRepository extends JpaRepository[WorkPeriod, java.lang.Long] {
  type WorkPeriods = java.util.List[WorkPeriod]
}
