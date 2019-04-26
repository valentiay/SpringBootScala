package com.ironworkman

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object IronWorkManApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[IronWorkManApplication], args: _*)
  }
}

@SpringBootApplication
trait IronWorkManApplication
