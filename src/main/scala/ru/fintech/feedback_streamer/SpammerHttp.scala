package ru.fintech.feedback_streamer

import akka.http.scaladsl.server.Directives._
import akka.actor._
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import spray.json._
import com.typesafe.config.ConfigFactory

/**
  * Created by Qman on 8/9/2016.
  */
trait SpamConfigRoutes {
  val routes = pathPrefix("spamconfig") {
    (path("get_responsibles") & get) {
      //TODO: implement this
      complete("stub")
    } ~ (path("update_responsibles") & post) {
      //TODO: implement this
      complete("stub")
    }
  }
}

object SpamConfigMicroservice extends App with SpamConfigRoutes {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  //TODO: Move interface and port to config
  Http().bindAndHandle(routes, "127.0.0.1", 8899)
}
