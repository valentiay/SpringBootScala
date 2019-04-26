package com.ironworkman

import com.ironworkman.db.{CategoryRepository, UserRepository, WorkPeriodRepository, WorkPeriodsDaysAndTimesRepository}
import com.ironworkman.telegramm.OldIronWorkManBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service


// Аналогично комментарию про OldIronWorkmanBot
@Service
case class TelegrammBotStarter @Autowired()(private val userRepository: UserRepository,
                                            private val workPeriodRepository: WorkPeriodRepository,
                                            private val workPeriodsDaysAndTimesRepository: WorkPeriodsDaysAndTimesRepository,
                                            private val categoryRepository: CategoryRepository)
  extends CommandLineRunner {

  override def run(args: String*): Unit = OldIronWorkManBot(userRepository,
                                                            workPeriodRepository,
                                                            workPeriodsDaysAndTimesRepository,
                                                            categoryRepository).run()
}
