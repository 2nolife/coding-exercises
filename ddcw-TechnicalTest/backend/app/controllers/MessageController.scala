package controllers

import javax.inject._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.mvc._
import services.MessageService

@Singleton
class MessageController @Inject()(messageService: MessageService, cc: ControllerComponents) extends AbstractController(cc) {

  val printMessage: Action[JsValue] = Action(parse.json) { implicit request =>
    Ok(JsObject(Seq("response" -> JsString(messageService.printToTerminal((request.body \ "message").as[String])))))
  }

}