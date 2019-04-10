package de.codecentric.microservice.db

import javax.persistence._
import org.springframework.data.jpa.repository.JpaRepository

import scala.beans.BeanProperty

@Entity
@Table(name = "USERS")
case class User(@BeanProperty userName: String) {

  def this() {
    this("")
    println("Test Constructor")
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  val id: Long = 0L
}


trait UserRepository extends JpaRepository[User, java.lang.Long] {
  type Users = java.util.List[User]
}
