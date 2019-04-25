package com.ironworkman

import com.ironworkman.db.{UserRepository, WorkPeriodRepository, WorkPeriodsDaysAndTimesRepository}
import com.ironworkman.telegramm.{OldIronWorkManBot, UpdatedIronWorkManBot}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class TelegrammBotStarter @Autowired()(private val userRepository: UserRepository,
                                       private val workPeriodRepository: WorkPeriodRepository,
                                       private val workPeriodsDaysAndTimesRepository: WorkPeriodsDaysAndTimesRepository)
    extends CommandLineRunner {
  override def run(args: String*): Unit = {
    val bot = new OldIronWorkManBot(userRepository, workPeriodRepository, workPeriodsDaysAndTimesRepository)
    bot.run()
  }
}
