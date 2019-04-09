package de.codecentric.microservice.db

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import scala.beans.BeanProperty

@Entity
case class Category(@BeanProperty name: String,
                    @BeanProperty description: String) {

  def this() {
    this("", "")
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L
}

@Repository
trait CategoryRepository extends CrudRepository[Category, java.lang.Long] {
}