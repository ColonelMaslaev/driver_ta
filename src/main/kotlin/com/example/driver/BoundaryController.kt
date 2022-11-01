package com.example.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController("/boundaries")
class BoundaryController @Autowired constructor(
    val boundaryDao: BoundaryDao
){
    @GetMapping("/boundaries/list")
    fun getBoundaries(): List<Boundary> {
        return boundaryDao.listBoundaries()
    }
    @PostMapping("/boundaries/add")
    fun insertBoundary(name: String, geometry: String) {
        boundaryDao.insertBoundary(geometry, name)
    }
    @GetMapping("/boundaries/driversinboundary")
    fun driverInBoundary(boundaryId: Int): List<Driver> {
        return boundaryDao.driversInBoundary(boundaryId)
    }
    @PostMapping("/boundaries/import")
    fun importBoundaries(fileName: String) {
        val text: String = File(fileName).readText()
        var startPosition: Int = text.indexOf("geometry")
        var endPosition: Int = text.indexOf("}", startIndex = startPosition)
        while (startPosition > 0) {
            val geometry: String = text.substring(startPosition + 10, endPosition + 1)
            startPosition = text.indexOf("name_1", startIndex = endPosition)
            endPosition = text.indexOf("hasc_1", startIndex = startPosition)
            val name: String = text.substring(startPosition + 9, endPosition - 3)
            boundaryDao.insertBoundary(geometry, name)
            println(geometry)
            startPosition = text.indexOf("geometry", startIndex = endPosition)
            endPosition = text.indexOf("}", startIndex = startPosition)
        }
    }
}