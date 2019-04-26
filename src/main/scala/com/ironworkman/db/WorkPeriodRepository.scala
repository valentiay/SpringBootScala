package com.ironworkman.db

import javax.persistence._
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import scala.annotation.meta.field
import scala.beans.BeanProperty
@Entity
@Table(name = "WORK_PERIOD")
case class WorkPeriod(@(Id @field)
                      @(GeneratedValue @field)
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

  def findByCategory_Id(id: Long): java.util.List[WorkPeriod]

  @Query(value = "select SUM(wp.timeAmount) from WorkPeriod wp where wp.category.id = ?1 group by wp.category.id")
  def findSumByCategoryIdAndUser2(id: Long): Long

  @Query(value = "select wp from WorkPeriod wp")
  def findSumByCategoryIdAndUser(): java.util.List[WorkPeriod]

}
