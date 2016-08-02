package ru.fintech.feedback_streamer

import javax.mail.internet.InternetAddress

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import courier.Defaults._
import courier._

/**
  * Created by Qman on 8/2/2016.
  */

sealed trait CommentSource
case object Bankiru extends CommentSource
case object Facebook extends CommentSource

object SpamActor {

  case class ProcessComment(src: CommentSource, link: String) // Signature to be replaced with actual structure

  case class UpdateResponsibles(newresp: Map[CommentSource, Seq[String]])

  case object GetResponsibles

  case class Responsibles(resp: Map[CommentSource, Seq[String]])

}


class SpamActor extends Actor {
  import SpamActor._

  def receive = {
    case ProcessComment(src, link) => responsibles(src) map {email => mailer(renderMessage(link, email))}
    case UpdateResponsibles(newresp) => responsibles = newresp
    case GetResponsibles => sender() ! Responsibles(responsibles)
  }

  private val conf = ConfigFactory.load()

  private val mailer = Mailer(conf.getString("spammer.sender.host"), conf.getInt("spammer.sender.port")) .auth(true) .as(conf.getString("spammer.sender.login"), conf.getString("spammer.sender.password")) .startTtls(conf.getBoolean("spammer.sender.startTtls"))()

  //This is not expected to grow beyond 1k elements, so I dont think we need to think about effectivness
  private var responsibles = Map[CommentSource, Seq[String]](
    Bankiru -> List.empty, 
    Facebook -> List.empty
  )

  private def renderMessage(link :String, to :String) = {
    val Array(fromAddr) = InternetAddress.parse(conf.getString("spammer.sender.host"))
    val Array(toAddr) = InternetAddress.parse(to)
    Envelope.from(fromAddr).to(toAddr).subject("New comment about tinkoff bank").content(Text(link))
  }
}
