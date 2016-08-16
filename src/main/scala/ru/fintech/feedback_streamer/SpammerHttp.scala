package ru.fintech.feedback_streamer

import akka.http.scaladsl.server.Directives._
import akka.actor._
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, FormData, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.MediaType
import akka.http.scaladsl.server.Directives
import spray.json._
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import akka.util.Timeout
import ru.fintech.feedback_streamer.SpamActor.RespStorage

import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by Qman on 8/9/2016.
  */

//========EARLY VERSION, NOT A SUBJECT TO REAL CODE-REVIEW======

//TODO:
// * email verification
// * marshalling
// * Set(None) to Set.empty
// * Report errors

trait SpamConfigRoutes extends DefaultJsonProtocol with SprayJsonSupport {
  val spamActor :ActorRef
  implicit val timeout: Timeout = 5 seconds
  val routes = pathPrefix("spamconfig") {
    (path("get_responsibles") & get) {
      val resp = Await.result( spamActor ? SpamActor.GetResponsibles, 5 seconds)
      resp match {
        case SpamActor.Responsibles(respmap) => {
          val facebook_emails = respmap(Facebook).mkString("\n")
          val bankiru_emails = respmap(Bankiru).mkString("\n")
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html.feedback_control(facebook_emails, bankiru_emails).toString))
        }
        case _ => complete("stub")
      }
    } ~ (path("update_responsibles") & post) {
      entity(as[FormData]) {
        request => {
          val newresp :RespStorage = Map(
            Facebook -> request.fields.get("facebook_resp").toString.split("\n").toSet,
            Bankiru -> request.fields.get("bankiru_resp").toString.split("\n").toSet
          )
          spamActor ! SpamActor.UpdateResponsibles(newresp)
          complete("stub")
        }
      }
    }
  }
}

object SpamConfigMicroservice extends App with SpamConfigRoutes {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  system.actorSelection("path")

  override val spamActor = system.actorOf(SpamActor.confProp)

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  //TODO: Move interface and port to config
  Http().bindAndHandle(routes, "127.0.0.1", 8899)
}
