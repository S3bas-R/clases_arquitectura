package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entities.Subject
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjetctNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// @Service: Sticker que dice que esta clase es el cerebro para controlar las materias.
@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    /**
     * Crea una nueva materia en el sistema.
     * 
     * @throws BlankNameException Si el nombre o código están en blanco.
     * @throws ProfessorNotFound Si el profesor asignado no existe.
     */
    fun createSubject(request: SubjectRequest): SubjectResponse {
        logger.info("Guardando nueva materia: ${request.name} con código ${request.code}")

        // Reglas de validación.
        if (request.name.isBlank()) {
            logger.warn("El nombre de la materia está vacío")
            throw BlankNameException()
        }
        if (request.code.isBlank()) {
            logger.warn("El código de la materia está vacío")
            throw BlankNameException()
        }

        // Buscamos si el profesor existe en el sistema.
        val professor = professorRepository.findById(request.professorId).orElseThrow {
            logger.warn("No se encontró el profesor con ID: ${request.professorId}")
            throw ProfessorNotFound()
        }

        val subject = request.toEntity()
        subject.professor = professor
        val saved = subjectRepository.save(subject)
        return saved.toResponse()
    }

    /**
     * Devuelve todas las materias registradas.
     */
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Obteniendo todas las materias")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    /**
     * Obtiene una materia por su identificador único.
     */
    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Buscando materia con ID: $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la materia con ID: $id")
            throw SubjetctNotFound()
        }
        return subject.toResponse()
    }

    /**
     * Actualiza los datos de una materia existente.
     */
    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        logger.info("Actualizando materia con ID: $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la materia para actualizar con ID: $id")
            throw SubjetctNotFound()
        }

        // Reglas de validación.
        if (request.name.isBlank()) {
            logger.warn("El nombre para actualizar la materia está vacío")
            throw BlankNameException()
        }
        if (request.code.isBlank()) {
            logger.warn("El código para actualizar la materia está vacío")
            throw BlankNameException()
        }

        // Buscamos si el nuevo profesor asignado existe.
        val professor = professorRepository.findById(request.professorId).orElseThrow {
            logger.warn("No se encontró el profesor con ID: ${request.professorId}")
            throw ProfessorNotFound()
        }

        val updatedSubject = Subject(
            id = subject.id,
            name = request.name,
            code = request.code,
            description = request.description,
            professor = professor
        )

        val saved = subjectRepository.save(updatedSubject)
        return saved.toResponse()
    }

    /**
     * Elimina una materia de la base de datos.
     */
    fun deleteSubject(id: Long) {
        logger.info("Eliminando materia con ID: $id")
        if (!subjectRepository.existsById(id)) {
            logger.warn("No se encontró la materia para eliminar con ID: $id")
            throw SubjetctNotFound()
        }
        subjectRepository.deleteById(id)
        logger.info("Materia con ID: $id eliminada correctamente")
    }
}
