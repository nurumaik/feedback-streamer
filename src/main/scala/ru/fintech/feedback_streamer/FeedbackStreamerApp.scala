package ru.fintech.feedback_streamer

import akka.actor.ActorSystem
import com.datastax.spark.connector.embedded.EmbeddedKafka

object FeedbackStreamerApp extends App {
  val system = ActorSystem()
  val app = new FeedbackStreamer(system)
}


class FeedbackStreamer(system: ActorSystem) {
  import system.dispatcher

  val settings = new CommentsMonitorSettings

  val kafka = new EmbeddedKafka()
}