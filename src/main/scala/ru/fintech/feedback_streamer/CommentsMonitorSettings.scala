package ru.fintech.feedback_streamer

import com.typesafe.config.{Config, ConfigFactory}

final class CommentsMonitorSettings(conf: Option[Config] = None) extends Serializable {

  protected val kafka = ConfigFactory.load.getConfig("kafka")
}
