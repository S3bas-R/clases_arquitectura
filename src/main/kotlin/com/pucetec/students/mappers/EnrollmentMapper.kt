package com.pucetec.students.mappers

import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment

/**
 * toResponse: Convierte una entidad de inscripción a su DTO de respuesta.
 * Mapea también de forma anidada al estudiante y la materia con sus respectivos mappers.
 */
fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,
    createdAt = this.createdAt.toString(),
    status = this.status,
    student = this.student.toResponse(),
    subject = this.subject.toResponse()
)
