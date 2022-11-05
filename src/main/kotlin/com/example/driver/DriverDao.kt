package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
class DriverDao @Autowired constructor(val jdbcTemplate: JdbcTemplate) {
    fun listDrivers(): List<Driver> {
        return jdbcTemplate.query("""SELECT driver_id, driver_name, driver_surname, timezone('UTC', driver_birthday),
            driver_address, ST_AsEWKT(driver_position), driver_status FROM driver""")
        {rs, _ -> Driver(rs.getInt(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7))}
    }
    fun insertDriver(name: String, surname: String, birthday: Long, address: String,
                     lat:String, lon: String, srid: String, status: Int) {
        val position = "SRID=$srid;POINT($lon $lat)"
        jdbcTemplate.update("""INSERT INTO driver(driver_name, driver_surname, driver_birthday, driver_address,
            driver_position, driver_status)
            VALUES (?, ?, ?, ?, ?::geometry, ?)""",
            name, surname, Timestamp(birthday), address, position, status)
    }
    fun removeDriver(id: Int) {
        jdbcTemplate.update("DELETE FROM driver WHERE driver_id = ?", id)
    }
    fun updatePosition(driverId: Int, lat: String, lon: String, srid: String) {
        val position = "SRID=$srid;POINT($lon $lat)"
        jdbcTemplate.update("UPDATE driver SET driver_position = ?::geometry WHERE driver_id = ?", position, driverId)
    }

    fun updateStatus(driverId: Int, statusId: Int) {
        jdbcTemplate.update("UPDATE driver SET driver_status = ? WHERE driver_id = ?", statusId, driverId)
    }
}
