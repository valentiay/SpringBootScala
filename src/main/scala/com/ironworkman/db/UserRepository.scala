package com.ironworkman.db

import scala.beans.BeanProperty
import javax.persistence._
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import scala.annotation.meta.field


@Entity
@Table(name = "USERS")
case class User(@(Id@field)
                @(GeneratedValue@field)
                @BeanProperty id: Long,

                @BeanProperty userName: String
               ) {

  def this() {
    this(0, "")
  }
}

@Repository
trait UserRepository extends CrudRepository[User, java.lang.Long] {
}

//@SequenceGenerator(name = "sUserId", sequenceName = "S_USER_ID", allocationSize = 1, initialValue = 2)