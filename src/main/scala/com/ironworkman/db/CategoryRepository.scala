package com.ironworkman.db

import javax.persistence._
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.{Controller, Repository, Service}

import scala.beans.BeanProperty

@Entity
@Table(name = "CATEGORIES")
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