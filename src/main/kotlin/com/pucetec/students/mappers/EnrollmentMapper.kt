package com.pucetec.students.mappers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment

fun EnrollmentRequest.toEntity() = Enrollment()

fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,
    createdAt = this.createdAt.toString(),
    status = this.status
)
