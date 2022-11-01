package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
class ShiftDao @Autowired constructor(val jdbcTemplate: JdbcTemplate) {
    fun listShifts(): List<Shift> {
        return jdbcTemplate.query("""SELECT shift_id, driver_id,
            timezone('UTC', shift_start), timezone('UTC', shift_end) FROM shift""")
        {rs, _ -> Shift(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4))}
    }
    fun insertShift(driverId: Int, shiftStart: Long, shiftEnd: Long) {
        jdbcTemplate.update("INSERT INTO shift(driver_id, shift_start, shift_end) VALUES (?, ?, ?)", driverId, Timestamp(shiftStart), Timestamp(shiftEnd))
    }
    fun shiftOverlap(intervalStart: Long, intervalEnd: Long): List<DriverShift> {
        return jdbcTemplate.query("""SELECT shift_id, d.driver_id, driver_name, driver_surname,
            timezone('UTC', shift_start), timezone('UTC', shift_end)
            FROM shift s, driver d
            WHERE s.driver_id = d.driver_id and s.shift_start <= ? and s.shift_end >= ?""",
                {rs, _ -> DriverShift(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6))},
                Timestamp(intervalStart), Timestamp(intervalEnd))
    }
}