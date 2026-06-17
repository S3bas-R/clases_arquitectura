package com.pucetec.students.services

// Importamos lo que necesitamos
import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentsRepository
import com.pucetec.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// @Service: El sticker de "Cerebro" para las inscripciones.
@Service
class EnrollmentServices(
    // Necesitamos 3 bibliotecarios para poder inscribir a alguien:
    private val enrollmentRepository: EnrollmentRepository, // Para guardar la inscripción.
    private val studentsRepository: StudentsRepository,     // Para buscar si el alumno existe.
    private val subjectRepository: SubjectRepository        // Para buscar si la materia existe.
) {
    // El diario para anotar qué pasa.
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * createEnrollment: Esta es la acción de inscribir a un alumno.
     */
    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Inscribiendo alumno ${request.studentId} en la materia ${request.subjectId}")

        // 1. Buscamos al alumno en la biblioteca. Si no está, el sistema grita (error).
        val student = studentsRepository.findById(request.studentId).orElseThrow()
        
        // 2. Buscamos la materia en la biblioteca. Si no está, el sistema grita (error).
        val subject = subjectRepository.findById(request.subjectId).orElseThrow()
        
        // 3. Creamos la "Ficha Oficial" (Entity) uniendo al alumno y la materia encontrados.
        val enrollment = Enrollment(
            student = student,
            subject = subject
        )
        
        // 4. Guardamos la ficha y la traducimos a una caja de salida para responder.
        return enrollmentRepository.save(enrollment).toResponse()
    }

    /**
     * getAllEnrollments: Devuelve la lista de todas las inscripciones.
     */
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Obteniendo todas las inscripciones")
        // Busca todas, y a cada una le aplica el traductor 'toResponse'.
        return enrollmentRepository.findAll().map { it.toResponse() }
    }
}
