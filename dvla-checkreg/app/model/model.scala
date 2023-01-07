package model

import java.util.Date

case class Vehicle(registrationNumber: String,
                   vehicleMake: String,
                   dateOfFirstRegistration: Date,
                   yearOfManufacture: Int,
                   cylinderCapacity: Int,
                   coEmissions: Int,
                   fuelType: String,
                   vehicleStatus: String,
                   vehicleColour: String,
                   vehicleTypeApproval: String,
                   wheelplan: String,
                   revenueWeight: Option[Int],
                   taxDue: Option[Date],
                   motExpires: Option[Date])
