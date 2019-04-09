package de.codecentric.microservice.db

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty


@Entity
case class WorkPeriod(@BeanProperty timeAmount: Long,
                      @BeanProperty description: String,
                      @BeanProperty workPeriodsDaysAndTimesWorkSchedulesId: Long,
                      @BeanProperty categoriesId: Long) {

  def this() {
    this(0, "", 0, 0)
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L
}

trait WorkPeriodRepository extends JpaRepository[WorkPeriod, java.lang.Long] {
  type WorkPeriods = java.util.List[WorkPeriod]
}

