package controllers

import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import views.html._

@Singleton
class EchoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  case class MyReply(regex: Int, commect: String)

  implicit val myReplyWrites: Writes[MyReply] = (
    (JsPath \ "regex").write[Int] and
    (JsPath \ "comment").write[String]
  )(unlift(MyReply.unapply))

  def echo(message: String): Action[AnyContent] = Action { 
    Ok(echopage(s"Received: $message"))
  }

  def regex(message: Int) = Action {
    Ok(Json.toJson(MyReply(message, "foo")))
  }

}