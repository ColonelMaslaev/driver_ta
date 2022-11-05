package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/drivers")
class DriverController @Autowired constructor(
		val driverDao: DriverDao
){
	@GetMapping("/drivers/list")
	fun getDrivers(): List<Driver> {
		return driverDao.listDrivers()
	}
	@PostMapping("/drivers/add")
	fun insertDriver(name: String, surname: String, birthday: Long, address: String,
					 lat: String, lon: String, srid: String, status: Int) {
		driverDao.insertDriver(name, surname, birthday, address, lat, lon, srid, status)
	}
	@DeleteMapping("/drivers/delete")
	fun removeDriver(id: Int) {
		driverDao.removeDriver(id)
	}
	@PostMapping("/drivers/updateposition")
	fun updatePosition(driverId: Int, lat: String, lon: String, srid: String) {
		driverDao.updatePosition(driverId, lat, lon, srid)
	}
	@PostMapping("drivers/updatestatus")
	fun updateStatus(driverId: Int, statusId: Int) {
		driverDao.updateStatus(driverId, statusId)
	}
}