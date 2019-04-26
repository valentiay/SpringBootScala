package com.ironworkman

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

object IronWorkManApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[IronWorkManApplication], args: _*)
  }
}

@SpringBootApplication
@EnableJpaRepositories
trait IronWorkManApplication
