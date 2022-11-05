package com.example.driver

data class DriverShift(val shiftId: Int,
                       val driverId: Int,
                       val driverName: String,
                       val driverSurname: String,
                       val shiftStart: String,
                       val shiftEnd: String)