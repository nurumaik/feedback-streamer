package ru.fintech.feedback_streamer

import javax.mail.internet.InternetAddress

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import com.typesafe.config.ConfigFactory
import courier.Defaults._
import courier._

/**
  * Created by Qman on 8/2/2016.
  */

//TODO: this is a stub, common types should not belong to my class
sealed trait CommentSource
case object Bankiru extends CommentSource
case object Facebook extends CommentSource

object SpamActor {

  //TODO: Should this type alias be there? Should I alias this at all?
  type RespStorage = Map[CommentSource, Set[String]]

  //TODO: This signature is a piece of shit, get rid asap
  case class ProcessComment(src: CommentSource, link: String)

  case class UpdateResponsibles(newresp: RespStorage)

  case object GetResponsibles

  case class Responsibles(resp: RespStorage)

  private val conf = ConfigFactory.load()
  private val senderConf = conf.getConfig("spammer.sender")

  private val defaultMailer = Mailer(senderConf.getString("host"), senderConf.getInt("port"))
    .auth(true)
    .as(senderConf.getString("login"), senderConf.getString("password"))
    .startTtls(senderConf.getBoolean("startTtls"))()

  val confProp = Props(classOf[SpamActor], defaultMailer)

}

//TODO: Should constructor expose inner usage of Mailer? Does props encapsulate it enough? Maybe make class private?
class SpamActor(private val mailer :Mailer) extends Actor {
  import SpamActor._

  val log = Logging(context.system, this)

  def receive = responsiblesDispatcher(defaultresponsibles)

  def responsiblesDispatcher(responsibles :RespStorage): Receive = {
    case ProcessComment(src, link) => {
      if (responsibles(src).isEmpty)
        log.warning(s"No handlers for source ${src.toString}")

      responsibles(src) foreach { email =>
        val err_f = {case msg :Throwable => log.error(s"Error while sending message to $email: ${msg.toString}") }
        mailer(renderMessage(link, email)).onFailure(err_f)
      }
    }
    case UpdateResponsibles(newresp) =>
      context become (responsiblesDispatcher(defaultresponsibles ++ newresp), discardOld = true)
    case GetResponsibles =>
      sender() ! Responsibles(responsibles)
  }

  //Can be some default handlers in future
  private val defaultresponsibles: RespStorage = Map(
    Bankiru -> Set.empty,
    Facebook -> Set.empty
  )

  //TODO: external CommentEmailRenderer class?
  private def renderMessage(link :String, to :String) = {
    val Array(fromAddr) = InternetAddress.parse(conf.getString("spammer.sender.host"))
    val Array(toAddr) = InternetAddress.parse(to)
    Envelope.from(fromAddr).to(toAddr).subject("New comment about tinkoff bank").content(Text(link))
  }
}
