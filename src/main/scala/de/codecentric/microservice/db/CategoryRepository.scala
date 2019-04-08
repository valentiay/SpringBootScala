package de.codecentric.microservice.db

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

import scala.beans.BeanProperty


@Entity
case class Category(@BeanProperty name: String, @BeanProperty description: String) {
  // default constructor for JPA
  def this() {
    this(null, null)
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L
}

trait CategoryRepository extends JpaRepository[Category, java.lang.Long] {
  type Categories = java.util.List[Category]

  //  def findByReaderIgnoreCase(@Param("reader") reader: String, pageable: Pageable): Books
  //  def findByIsbnIgnoreCase(@Param("isbn") isbn: String, pageable: Pageable): Books
}