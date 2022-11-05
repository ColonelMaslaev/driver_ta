package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class BoundaryDao @Autowired constructor(val jdbcTemplate: JdbcTemplate) {
    fun listBoundaries(): List<Boundary> {
        return jdbcTemplate.query("SELECT boundary_id, boundary_name, ST_AsEWKT(boundary_geometry) FROM boundary")
        { rs, _ -> Boundary(rs.getInt(1), rs.getString(2), rs.getString(3))}
    }
    fun insertBoundary(geometry: String, name: String) {
        jdbcTemplate.update("INSERT INTO boundary(boundary_name, boundary_geometry) VALUES (?, ST_GeomFromGeoJSON(?))", name, geometry)
    }
    fun driversInBoundary(boundaryId: Int): List<Driver> {
        return jdbcTemplate.query("""SELECT driver_id, driver_name, driver_surname, timezone('UTC', driver_birthday), driver_address, ST_AsEWKT(driver_position), driver_status
            FROM driver WHERE ST_Contains((SELECT boundary_geometry FROM boundary WHERE boundary_id = ?), driver_position)""",
                {rs, _ -> Driver(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7))},
                boundaryId)
    }
}