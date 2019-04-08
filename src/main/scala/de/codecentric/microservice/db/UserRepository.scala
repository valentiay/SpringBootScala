package de.codecentric.microservice.db

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty

@Entity
case class User(@BeanProperty userName: String) {
  // default constructor for JPA
  def this() {
    this(null, null)
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  val id: Long = 0L
}


trait UserRepository extends JpaRepository[User, java.lang.Long] {
  type Users = java.util.List[User]

  //  def findByReaderIgnoreCase(@Param("reader") reader: String, pageable: Pageable): Books
  //  def findByIsbnIgnoreCase(@Param("isbn") isbn: String, pageable: Pageable): Books
}
