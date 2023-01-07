package controllers

import actors.{ChatActor, ChatManager}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class WebSocketChat @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  val manager = system.actorOf(Props[ChatManager], name = "Manager")
  
  def index = Action { implicit request =>
    Ok(views.html.chatPage())
  }

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, manager)
    }
  }
  
}
