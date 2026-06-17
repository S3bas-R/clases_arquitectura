package com.pucetec.students.mappers

// Importamos lo que necesitamos
import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment

/**
 * ⚠️ NOTA IMPORTANTE:
 * EnrollmentRequest no tiene suficientes datos para crear un Enrollment completo
 * porque faltan los objetos de Student y Subject.
 * La conversión real se hace en EnrollmentServices.kt usando los Repositorios.
 * 
 * Por ahora, dejamos esta función comentada o lanzamos un error si se usa mal.
 */
// fun EnrollmentRequest.toEntity() = ... // Se hace en el Service

/**
 * toResponse: Traduce la ficha oficial de inscripción a una caja de salida.
 */
fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,                     // Copiamos el número de inscripción.
    createdAt = this.createdAt.toString(), // Convertimos la fecha a texto.
    status = this.status               // Copiamos el estado (ej: ACTIVE).
)
