package ru.igrocki.im_a_good

import akka.actor.ActorSystem

object CommentsMonitorApp extends App {

}


class CommentsMonitor(system: ActorSystem) {
  import system.dispatcher

  val settings = new CommentsMonitorSettings
}