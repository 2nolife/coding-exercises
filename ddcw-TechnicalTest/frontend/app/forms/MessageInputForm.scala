package forms

import models.MessageInput
import play.api.data.Form
import play.api.data.Forms.{mapping, text}

object MessageInputForm {
  val messageInputForm: Form[MessageInput] = {
    Form(mapping(
      "message" -> text
        .verifying("message.message-input.form.need-value", _.nonEmpty)
    )(MessageInput.apply)(MessageInput.unapply))
  }
}