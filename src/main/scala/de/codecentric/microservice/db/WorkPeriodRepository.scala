package de.codecentric.microservice.db

import javax.persistence._
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty


@Entity
@Table(name = "WORK_PERIOD")
case class WorkPeriod(@BeanProperty timeAmount: Long,
                      @BeanProperty description: String) {

  def this() {
    this(0, "")
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L

  @ManyToOne
  @JoinColumn(name = "categoriesId", nullable = true)
  @BeanProperty
  var category: Category = _

  @ManyToOne
  @JoinColumn(name = "workPeriodsDaysAndTimesId", nullable = true)
  @BeanProperty
  var workPeriodsDaysAndTimes: WorkPeriodsDaysAndTimes = _


}

trait WorkPeriodRepository extends JpaRepository[WorkPeriod, java.lang.Long] {
  type WorkPeriods = java.util.List[WorkPeriod]
}

