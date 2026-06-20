package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.entities.Professor
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// @Service: Dice que esta clase es el cerebro para controlar a los profesores.
@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    /**
     * Crea un profesor nuevo en la base de datos.
     */
    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        logger.info("Guardando nuevo profesor: ${request.name}")

        // Regla: El nombre del profesor no puede estar vacío.
        if (request.name.isBlank()) {
            logger.warn("El nombre del profesor está en blanco")
            throw BlankNameException()
        }

        val professor = request.toEntity()
        val saved = professorRepository.save(professor)
        return saved.toResponse()
    }

    /**
     * Devuelve todos los profesores de la base de datos.
     */
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Obteniendo todos los profesores")
        return professorRepository.findAll().map { it.toResponse() }
    }

    /**
     * Busca un profesor usando su identificador único.
     */
    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Buscando profesor con ID: $id")
        val professor = professorRepository.findById(id).orElseThrow {
            logger.warn("No se encontró el profesor con ID: $id")
            throw ProfessorNotFound()
        }
        return professor.toResponse()
    }

    /**
     * Actualiza el nombre y/o correo de un profesor.
     */
    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        logger.info("Actualizando profesor con ID: $id")
        val professor = professorRepository.findById(id).orElseThrow {
            logger.warn("No se encontró el profesor para actualizar con ID: $id")
            throw ProfessorNotFound()
        }

        // Regla: El nombre no puede estar en blanco.
        if (request.name.isBlank()) {
            logger.warn("El nombre proporcionado para la actualización está en blanco")
            throw BlankNameException()
        }

        // Creamos una nueva entidad con los datos actualizados y la misma clave primaria.
        val updatedProfessor = Professor(
            id = professor.id,
            name = request.name,
            email = request.email,
            subjects = professor.subjects
        )

        val saved = professorRepository.save(updatedProfessor)
        return saved.toResponse()
    }

    /**
     * Elimina a un profesor del sistema.
     */
    fun deleteProfessor(id: Long) {
        logger.info("Eliminando profesor con ID: $id")
        if (!professorRepository.existsById(id)) {
            logger.warn("No se encontró el profesor para eliminar con ID: $id")
            throw ProfessorNotFound()
        }
        professorRepository.deleteById(id)
        logger.info("Profesor con ID: $id eliminado correctamente")
    }
}
