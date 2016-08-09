package ru.fintech.feedback_streamer

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender }
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll

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
    }
    "Return same responsibles it just got from set operation" in {
    }
    //TODO: maybe it should check emails for validness or some other shit at this stage?
  }
}

