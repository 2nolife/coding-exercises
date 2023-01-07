package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class VehicleData(registration: String, make: String, reference: Option[String])

object Forms {

  val registrationRegEx = "^(([A-Za-z]{1,2}[ ]?[0-9]{1,4})|([A-Za-z]{3}[ ]?[0-9]{1,3})|([0-9]{1,3}[ ]?[A-Za-z]{3})|([0-9]{1,4}[ ]?[A-Za-z]{1,2})|([A-Za-z]{3}[ ]?[0-9]{1,3}[ ]?[A-Za-z])|([A-Za-z][ ]?[0-9]{1,3}[ ]?[A-Za-z]{3})|([A-Za-z]{2}[ ]?[0-9]{2}[ ]?[A-Za-z]{3})|([A-Za-z]{3}[ ]?[0-9]{4}))$"
  val referenceRegEx = "^[0-9]{11}$"

  val vehicleForm = Form(
    mapping(
      "registration" -> text
        .verifying(error = "Please enter your registration number", v => v.nonEmpty)
        .verifying(pattern(registrationRegEx.r, error = "You must enter your registration number in a valid format")),
      "make" -> text
        .verifying(error = "You must select your vehicle make from the list", v => v.nonEmpty),
      "reference" -> optional(text
        .verifying(pattern(referenceRegEx.r, error = "Your reference number should contain 11 digits only")))
    )(VehicleData.apply)(VehicleData.unapply)
  )

}
