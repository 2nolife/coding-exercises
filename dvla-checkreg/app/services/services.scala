package services

import java.text.SimpleDateFormat
import java.util.Locale

import model.Vehicle

trait SearchService {
  def search(registration: String, make: String): Option[Vehicle]
}

class SearchServiceImpl extends SearchService {

  override def search(registration: String, make: String): Option[Vehicle] = {
    (registration.replaceAll(" ", "").toUpperCase, make.toUpperCase) match {
      case ("AE64RAU", "MINI") => Some(myNewMiniVehicle)
      case ("AE64OLD", "MINI") => Some(myOldMiniVehicle)
      case _ => None
    }
  }

  private val sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

  // AE64 RAU - MINI
  private def myNewMiniVehicle =
    Vehicle(
      "AE64 RAU",
      "MINI",
      sdf.parse("05 September 2014"),
      2014,
      1496,
      98,
      "DIESEL",
      "Tax not due", // woohoo - 0 pounds road tax!
      "ORANGE",
      "M1",
      "2 AXLE RIGID BODY",
      None,
      Some(sdf.parse("01 September 2016")),
      None
    )

  // AE64 OLD - MINI
  private def myOldMiniVehicle =
    Vehicle(
      "AE64 OLD",
      "MINI",
      sdf.parse("05 September 2010"),
      2007,
      1796,
      115,
      "DIESEL",
      "Tax is due",
      "RED",
      "M1",
      "2 AXLE RIGID BODY",
      Some(3500),
      Some(sdf.parse("01 September 2014")),
      Some(sdf.parse("10 October 2014"))
    )

}

