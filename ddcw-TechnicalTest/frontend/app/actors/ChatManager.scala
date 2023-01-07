package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable.ListBuffer

class ChatManager extends Actor {
  import ChatManager._

  private val chatters = new ListBuffer[ActorRef]

  override def receive = {
    case NewChatter(chatter) => chatters += chatter
    case Message(msg) => for (c <- chatters) c ! ChatActor.SendMessage(msg)
  }

}

object ChatManager {

  case class NewChatter(chatter: ActorRef)
  case class Message(msg: String)

}