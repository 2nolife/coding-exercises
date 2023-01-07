package controllers

import javax.inject._

import connectors.BackendConnector
import forms.MessageInputForm.messageInputForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import views.html._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MessageController @Inject()(backendConnector: BackendConnector,
                                  override val messagesApi: MessagesApi,
                                  cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val getHomepage: Action[AnyContent] = Action { implicit request =>
    val maybeMessage: String = request.session.get("MESSAGE") getOrElse ""
    Ok(homepage(messageInputForm, maybeMessage)).removingFromSession("MESSAGE")
  }

  val submitMessage: Action[AnyContent] = Action.async { implicit request =>
    messageInputForm.bindFromRequest().fold(
      formWithErrors => {
        Future successful BadRequest(homepage(formWithErrors, ""))
      }, userMessage => {
        backendConnector.sendMessage(userMessage.message).map { response =>
          Redirect(routes.MessageController.getHomepage).addingToSession("MESSAGE" -> response)
        }
      }
    )
  }
}