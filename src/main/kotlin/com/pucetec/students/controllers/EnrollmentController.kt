package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.services.EnrollmentServices
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentServices: EnrollmentServices
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createEnrollment(@RequestBody request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment for student ${request.studentId} in subject ${request.subjectId}")
        return enrollmentServices.createEnrollment(request)
    }

    @GetMapping
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentServices.getAllEnrollments()
    }
}
