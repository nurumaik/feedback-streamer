package ru.fintech.feedback_streamer

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.jvnet.mock_javamail.Mailbox
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Inside._
import scala.concurrent._
import scala.concurrent.duration._

class MailSpec() extends TestKit(ActorSystem("MailSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A Spam actor" must {
    //TODO: restructure tests
    //Comment processing
    "Send an email containing link to comment if target is specified as responsibles for that source" in {
      val spamActor = system.actorOf(SpamActor.confProp)
      spamActor ! SpamActor.UpdateResponsibles(Map(Bankiru -> Set("vasya@vasya.ru", "petya@petya.com")))
      spamActor ! SpamActor.ProcessComment(Bankiru, "some link")
      Thread.sleep(1000)
      val petyainbox = Mailbox.get("petya@petya.com")
      petyainbox.size shouldBe 1
      val msg = petyainbox.get(0)
      inside(msg.getContent) {
        case s: String => s should include ("some link")
      }
    }
    "Not send an email to targets not specified as responsibles for that source " in {
      val spamActor = system.actorOf(SpamActor.confProp)
      spamActor ! SpamActor.UpdateResponsibles(Map(Bankiru -> Set("vasya@vasya.ru", "petya@petya.ru"), Facebook -> Set.empty))
      spamActor ! SpamActor.ProcessComment(Facebook, "some link")
      Thread.sleep(1000)
      val petyainbox = Mailbox.get("petya@petya.ru")
      petyainbox.size shouldBe 0
    }

    //Responsibles management
    "Start with empty responsibles for each source" in {
      val spamActor = system.actorOf(SpamActor.confProp)
      within(1 second) {
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(Map(Bankiru -> Set.empty, Facebook -> Set.empty)))
        expectNoMsg
      }
    }

    "Return same responsibles it just got from set operation" in {
      val spamActor = system.actorOf(SpamActor.confProp)
      val responsibles1 :SpamActor.RespStorage = Map(
        Bankiru -> Set("vasya@vasya.ru", "petya@petya.com"),
        Facebook -> Set("vasya@vasya.ru", "sasha@sasha.ua"))
      val responsibles2 :SpamActor.RespStorage = Map(
        Bankiru -> Set("masha@masha.ru", "petya@petya.com"),
        Facebook -> Set.empty)
      val responsibles3 :SpamActor.RespStorage = responsibles2 - Facebook

      within(1 second) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles1)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles1))
        expectNoMsg
      }

      within(1 second) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles2)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles2))
        expectNoMsg
      }

      within(1 second) {
        spamActor ! SpamActor.UpdateResponsibles(responsibles3)
        spamActor ! SpamActor.GetResponsibles
        expectMsg(SpamActor.Responsibles(responsibles2))
        expectNoMsg
      }
    }
  }
}

