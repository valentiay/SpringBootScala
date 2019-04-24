package com.ironworkman.controller

import com.ironworkman.db.CategoryRepository
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/categories"))
class CategoryController(private val categoryRepository: CategoryRepository) {

  @GetMapping(produces = Array("application/json"))
  def getAllCategory() = categoryRepository.findAll()
}
