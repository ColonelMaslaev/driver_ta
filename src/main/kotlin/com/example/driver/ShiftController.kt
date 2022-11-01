package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/shifts")
class ShiftController @Autowired constructor(
    val shiftDao: ShiftDao
){
    @GetMapping("/shifts/list")
    fun getShifts(): List<Shift> {
        return shiftDao.listShifts()
    }
    @PostMapping("/shifts/add")
    fun insertShift(driverId: Int, shiftStart: Long, shiftEnd: Long) {
        shiftDao.insertShift(driverId, shiftStart, shiftEnd)
    }
    @GetMapping("/shifts/contain")
    fun shiftsOverlap(intervalStart: Long, intervalEnd: Long): List<DriverShift> {
        return shiftDao.shiftOverlap(intervalStart, intervalEnd)
    }
}