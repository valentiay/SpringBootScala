package de.codecentric.microservice.controller


import de.codecentric.microservice.db.CategoryRepository
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/categories"))
class CategoryController(private val categoryRepository: CategoryRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllCategory() = {
    println("Test")
    categoryRepository.findAll()
  }
}