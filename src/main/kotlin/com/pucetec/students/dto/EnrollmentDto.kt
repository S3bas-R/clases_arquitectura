package com.pucetec.students.dto

/**
 * Representa los datos enviados para crear una inscripción.
 */
data class EnrollmentRequest(
    val studentId: Long,
    val subjectId: Long,
)

/**
 * Representa los datos enviados para actualizar el estado de una inscripción.
 */
data class EnrollmentUpdateRequest(
    val status: String,
)

/**
 * Representa la respuesta de una inscripción, incluyendo datos detallados del
 * estudiante y la materia inscrita.
 */
data class EnrollmentResponse(
    val id: Long,
    val createdAt: String,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse,
)
