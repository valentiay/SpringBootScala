package com.ironworkman.telegramm

import info.mukel.telegrambot4s._, api._, methods._, models._, Implicits._

object IronWorkManBot extends TelegramBot with Polling with Commands {
  def token = "767996938:AAF6talqUn--PI0z2vJeAxcOtvMRWrQkevw"

  override def onMessage(message: Message) = message.text match {
    case Some(text) => request(SendMessage(message.chat.id, text))
    case _ => println("It had not a text")
  }

  def main(args: Array[String]): Unit = {
    IronWorkManBot.run()
  }
}