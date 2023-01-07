package forms

import play.api.libs.json.Json
import support.UnitSpec
import forms.MessageInputForm.messageInputForm
import models.MessageInput
import play.api.data.FormError

class MessageInputFormSpec extends UnitSpec {
  val messageEmptyMessage: String = "message.message-input.form.need-value"
  val messageEmptyFormError: FormError = FormError("message", List(messageEmptyMessage))

  "return no errors with valid data" in {
    val postData = Json.obj("message" → "test1")

    val validatedForm = messageInputForm.bind(postData)

    assert(validatedForm.errors.isEmpty)
  }

  "return no errors with valid data (proper)" in {
    val postData = Json.obj("message" -> "test1")

    val validatedForm = messageInputForm.bind(postData, 100)

    validatedForm.errors shouldBe Seq.empty
  }

  "return an error when input field is left blank" in {
    val postData = Json.obj("message" → "")

    val validatedForm = messageInputForm.bind(postData)

    assert(validatedForm.errors.contains(messageEmptyFormError))
  }

  "return an error when input field is left blank (proper)" in {
    val postData = Json.obj("message" -> "")

    val validatedForm = messageInputForm.bind(postData, 100)

    validatedForm.errors should contain (messageEmptyFormError)
  }

  "return no errors when unbinding the form" in {
    val unboundForm = messageInputForm.mapping.unbind(MessageInput("test1"))

    unboundForm("message") shouldBe "test1"
  }
}