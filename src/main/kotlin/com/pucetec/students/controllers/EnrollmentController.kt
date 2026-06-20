package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.services.EnrollmentServices
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// @RestController: Dice que esta clase maneja las peticiones web (HTTP) de las inscripciones.
@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentServices: EnrollmentServices
) {
    private val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    // @PostMapping: Crea una nueva inscripción. Responde con 201 (Creado).
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEnrollment(@RequestBody request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Recibida petición para inscribir estudiante ${request.studentId} en materia ${request.subjectId}")
        return enrollmentServices.createEnrollment(request)
    }

    // @GetMapping: Obtiene la lista de todas las inscripciones. Responde con 200 (OK).
    @GetMapping
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Recibida petición para listar todas las inscripciones")
        return enrollmentServices.getAllEnrollments()
    }

    // @GetMapping("/{id}"): Obtiene una inscripción por su ID único. Responde con 200 (OK).
    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): EnrollmentResponse {
        logger.info("Recibida petición para obtener inscripción con ID: $id")
        return enrollmentServices.getEnrollmentById(id)
    }

    // @PutMapping("/{id}"): Actualiza el estado (status) de una inscripción. Responde con 200 (OK).
    @PutMapping("/{id}")
    fun updateEnrollment(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentUpdateRequest
    ): EnrollmentResponse {
        logger.info("Recibida petición para actualizar el estado de la inscripción con ID: $id")
        return enrollmentServices.updateEnrollment(id, request)
    }

    // @DeleteMapping("/{id}"): Elimina una inscripción por su ID. Responde con 204 (Sin contenido).
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEnrollment(@PathVariable id: Long) {
        logger.info("Recibida petición para eliminar inscripción con ID: $id")
        enrollmentServices.deleteEnrollment(id)
    }
}
