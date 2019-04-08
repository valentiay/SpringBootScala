package de.codecentric.microservice.db

import java.sql.Timestamp

import javax.persistence.Entity
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty

@Entity
case class WorkPeriodsDaysAndTimes(@BeanProperty startDayTime: Timestamp,
                                   @BeanProperty endDayTime: Timestamp,
                                   @BeanProperty periodMin: Long,
                                   @BeanProperty usersId:Long) {
  // default constructor for JPA
  def this() {
    this(null, null)
  }
}

trait WorkPeriodsDaysAndTimesRepository extends JpaRepository[WorkPeriodsDaysAndTimes, java.lang.Long] {
  type WorkPeriodsDaysAndTimesList = java.util.List[WorkPeriodsDaysAndTimes]

  //  def findByReaderIgnoreCase(@Param("reader") reader: String, pageable: Pageable): Books
  //  def findByIsbnIgnoreCase(@Param("isbn") isbn: String, pageable: Pageable): Books
}
