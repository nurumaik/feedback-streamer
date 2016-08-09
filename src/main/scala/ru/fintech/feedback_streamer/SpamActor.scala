package ru.fintech.feedback_streamer

import javax.mail.internet.InternetAddress

import akka.actor.Actor
import akka.actor.Props
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

  private val defaultMailer = Mailer(conf.getString("spammer.sender.host"), conf.getInt("spammer.sender.port"))
    .auth(true)
    .as(conf.getString("spammer.sender.login"), conf.getString("spammer.sender.password"))
    .startTtls(conf.getBoolean("spammer.sender.startTtls"))()

  val confProp = Props(classOf[SpamActor], defaultMailer)

}

//TODO: Should constructor expose inner usage of Mailer? Does props encapsulate it enough? Maybe make class private?
class SpamActor(private val mailer :Mailer) extends Actor {
  import SpamActor._

  def receive = {
    case ProcessComment(src, link) => responsibles(src) map {email => mailer(renderMessage(link, email))}
    case UpdateResponsibles(newresp) => responsibles = newresp
    case GetResponsibles => sender() ! Responsibles(responsibles)
  }

  //This is not expected to grow beyond 1k elements, so I dont think we need to think about effectivness
  //TODO: Maybe we can make it more IMMUTABLE and YOBA implementing responsibles change through BECOME instead of var
  private var responsibles: RespStorage = Map(
    Bankiru -> Set.empty,
    Facebook -> Set.empty
  )

  private def renderMessage(link :String, to :String) = {
    val Array(fromAddr) = InternetAddress.parse(conf.getString("spammer.sender.host"))
    val Array(toAddr) = InternetAddress.parse(to)
    Envelope.from(fromAddr).to(toAddr).subject("New comment about tinkoff bank").content(Text(link))
  }
}