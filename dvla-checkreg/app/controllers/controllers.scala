package controllers

import forms.Forms._
import forms.VehicleData
import play.api._
import play.api.data.Form
import play.api.mvc._
import play.api.mvc.Results._
import Constants._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.{SearchService, SearchServiceImpl}

import scala.concurrent.Future

object Constants {
  val pageTitleSuffix = "- GOV.UK"
}

class Application extends Controller {

  val searchService: SearchService = new SearchServiceImpl

  def index = Action {
    Ok(views.html.index(s"Get vehicle information from DVLA $pageTitleSuffix"))
  }

  def finish = Action {
    Ok(views.html.finish(s"Thank you $pageTitleSuffix"))
  }

  val enquiryTitle = s"Details of the vehicle being checked $pageTitleSuffix"

  def enquiry = Action {
    Ok(views.html.enquiry(title = enquiryTitle, form = vehicleForm))
  }

  def search = Action.async { implicit request =>
    vehicleForm.bindFromRequest.fold(
      erform => {
        Future.successful(BadRequest(views.html.enquiry(enquiryTitle, erform)))
      },
      vehicleData => {
        val f = Future { searchService.search(vehicleData.registration, vehicleData.make) } // non blocking
        f.map { vehicle =>
          Ok {
            vehicle
              .map(x => views.html.found(enquiryTitle, vehicle = x))
              .getOrElse(views.html.notfound(enquiryTitle))
          }
        }
      }
    )
  }

  /* same as above but I had difficulties testing this (it looks cool) */
  def search2 = Action.async(parse.form(vehicleForm, onErrors = (erform: Form[VehicleData]) => BadRequest(views.html.enquiry(enquiryTitle, erform)))) { implicit request =>
    val vehicleData = request.body
    val f = Future { searchService.search(vehicleData.registration, vehicleData.make) } // non blocking
    f.map { vehicle =>
      Ok {
        vehicle
          .map(x => views.html.found(enquiryTitle, vehicle = x))
          .getOrElse(views.html.notfound(enquiryTitle))
      }
    }
  }

}

object Global extends GlobalSettings {

  override def onHandlerNotFound(request: RequestHeader): Future[Result] = {
    Future(NotFound(views.html.error404(s"Page not found - 404 $pageTitleSuffix")))
  }

}