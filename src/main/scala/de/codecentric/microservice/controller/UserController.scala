package de.codecentric.microservice.controller

import de.codecentric.microservice.db.{User, UserRepository}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/users"))
class UserController(private val userRepository: UserRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllUser() = userRepository.findAll()

  @PostMapping(produces = Array("application/json"), consumes = Array("application/json"))
  def addUser(@RequestBody user: User) = {
    println("Test")
    userRepository.save(user)
  }

}