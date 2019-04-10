package de.codecentric.microservice.db

import java.sql.Timestamp

import javax.persistence._
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty

@Entity
@Table(name = "WORK_PERIODS_DAYS_AND_TIMES")
case class WorkPeriodsDaysAndTimes(@BeanProperty startDayTime: Timestamp,
                                   @BeanProperty endDayTime: Timestamp,
                                   @BeanProperty periodMin: Long) {
  def this() {
    this(null, null, 0)
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L

  @ManyToOne
  @JoinColumn(name = "usersId", nullable = true)
  @BeanProperty
  var user: User = _
}

trait WorkPeriodsDaysAndTimesRepository extends JpaRepository[WorkPeriodsDaysAndTimes, java.lang.Long] {
  type WorkPeriodsDaysAndTimesList = java.util.List[WorkPeriodsDaysAndTimes]
}
