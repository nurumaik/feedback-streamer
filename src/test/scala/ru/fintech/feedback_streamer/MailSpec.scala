package ru.fintech.feedback_streamer

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import scala.concurrent._
import scala.concurrent.duration._

class MailSpec() extends TestKit(ActorSystem("MailSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A Spam actor" must {
    //TODO: make more levels of tests
    //Comment processing
    "Send an email containing link to comment if target is specified as responsibles for that source" in {
    }
    "Not send an email to targets not specified as responsibles for that source " in {
    }
    //TODO: think about error reporting. Logs?
    "Report an error if there is some trouble sending an email" in {
    }
    "Report a warning if there is no responsibles for source" in {
    }

    //Responsibles management
    "Start with empty responsibles for each source" in {
      val spamActor = system.actorOf(SpamActor.confProp)
      within(5 seconds) {
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(Map(Bankiru -> Set.empty, Facebook -> Set.empty)))
        expectNoMsg
      }
    }

    "Return same responsibles it just got from set operation" in {
      //TODO: maybe it should check emails for validness or some other shit at this stage?
      val spamActor = system.actorOf(SpamActor.confProp)
      val responsibles1 :SpamActor.RespStorage = Map(
        Bankiru -> Set("vasya@vasya.ru", "petya@petya.com"),
        Facebook -> Set("vasya@vasya.ru", "sasha@sasha.ua"))
      val responsibles2 :SpamActor.RespStorage = Map(
        Bankiru -> Set("masha@masha.ru", "petya@petya.com"),
        Facebook -> Set.empty)

      val responsibles3 :SpamActor.RespStorage = responsibles2 - Facebook

      //TODO: replace this boilerplate with something readable
      within(5 seconds) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles1)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles1))
        expectNoMsg
      }

      within(5 seconds) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles2)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles2))
        expectNoMsg
      }

      within(5 seconds) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles3)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles2))
        expectNoMsg
      }
    }
  }
}

