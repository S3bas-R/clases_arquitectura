package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentsRepository
import com.pucetec.students.repositories.SubjectRepository
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.exceptions.EnrollmentNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @Service: Sticker de Spring que dice que esta clase controla las inscripciones de los estudiantes en las materias.
 */
@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentsRepository: StudentsRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    /**
     * Inscribe a un estudiante existente en una materia existente.
     * El estado por defecto inicial es "INSCRITO".
     * 
     * @param request Datos de la inscripción (estudiante y materia).
     * @return Respuesta de la inscripción creada.
     * @throws StudentNotFoundException Si el estudiante no existe.
     * @throws SubjectNotFound Si la materia no existe.
     */
    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creando inscripción para el estudiante: ${request.studentId} en la materia: ${request.subjectId}")

        // 1. Buscamos si el estudiante existe.
        val student = studentsRepository.findById(request.studentId).orElseThrow {
            logger.warn("No se encontró el estudiante con ID: ${request.studentId}")
            throw StudentNotFoundException()
        }

        // 2. Buscamos si la materia existe.
        val subject = subjectRepository.findById(request.subjectId).orElseThrow {
            logger.warn("No se encontró la materia con ID: ${request.subjectId}")
            throw SubjectNotFound()
        }

        // 3. Creamos la inscripción con estado inicial "INSCRITO".
        val enrollment = Enrollment(
            status = "INSCRITO",
            createdAt = LocalDateTime.now(),
            student = student,
            subject = subject
        )

        val saved = enrollmentRepository.save(enrollment)
        logger.info("Inscripción guardada correctamente con ID: ${saved.id}")
        return saved.toResponse()
    }

    /**
     * Obtiene todas las inscripciones del sistema.
     */
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Obteniendo todas las inscripciones del sistema")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    /**
     * Busca una inscripción por su ID.
     * 
     * @param id Identificador único de la inscripción.
     * @return La inscripción correspondiente.
     * @throws EnrollmentNotFound Si no existe la inscripción.
     */
    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Buscando inscripción con ID: $id")
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la inscripción con ID: $id")
            throw EnrollmentNotFound()
        }
        return enrollment.toResponse()
    }

    /**
     * Actualiza únicamente el estado ("status") de una inscripción.
     * 
     * @param id Identificador único de la inscripción.
     * @param request Datos de actualización (nuevo status).
     * @return La inscripción actualizada.
     * @throws EnrollmentNotFound Si no existe la inscripción.
     */
    fun updateEnrollment(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        logger.info("Actualizando el estado de la inscripción con ID: $id a: ${request.status}")
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la inscripción para actualizar con ID: $id")
            throw EnrollmentNotFound()
        }

        // Modificamos directamente el estado de la inscripción
        enrollment.status = request.status

        val saved = enrollmentRepository.save(enrollment)
        logger.info("Inscripción con ID: $id actualizada con éxito")
        return saved.toResponse()
    }

    /**
     * Elimina una inscripción de la base de datos.
     * 
     * @param id Identificador de la inscripción a eliminar.
     * @throws EnrollmentNotFound Si no existe la inscripción.
     */
    fun deleteEnrollment(id: Long) {
        logger.info("Eliminando inscripción con ID: $id")
        if (!enrollmentRepository.existsById(id)) {
            logger.warn("No se encontró la inscripción para eliminar con ID: $id")
            throw EnrollmentNotFound()
        }
        enrollmentRepository.deleteById(id)
        logger.info("Inscripción con ID: $id eliminada correctamente")
    }
}
