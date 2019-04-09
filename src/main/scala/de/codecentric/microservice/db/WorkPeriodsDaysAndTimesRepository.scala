package de.codecentric.microservice.db

import java.sql.Timestamp

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty

@Entity
case class WorkPeriodsDaysAndTimes(@BeanProperty startDayTime: Timestamp,
                                   @BeanProperty endDayTime: Timestamp,
                                   @BeanProperty periodMin: Long,
                                   @BeanProperty usersId: Long) {
  def this() {
    this(null, null, 0, 0)
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val workSchedulesId: Long = 0L

}

trait WorkPeriodsDaysAndTimesRepository extends JpaRepository[WorkPeriodsDaysAndTimes, java.lang.Long] {
  type WorkPeriodsDaysAndTimesList = java.util.List[WorkPeriodsDaysAndTimes]
}
