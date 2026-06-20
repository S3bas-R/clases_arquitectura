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

/**
 * @Service: Dice que esta clase es el cerebro para controlar a los profesores del sistema.
 */
@Service
class ProfessorService(
    // Inyectamos el repositorio del profesor para interactuar con la base de datos.
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    /**
     * Crea un profesor nuevo en la base de datos.
     * 
     * @param request Datos del profesor a registrar.
     * @return El profesor creado y formateado para la respuesta.
     * @throws BlankNameException Si el nombre del profesor está vacío.
     */
    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        logger.info("Iniciando la creación del profesor: ${request.name}")

        // Regla: El nombre del profesor no puede estar vacío.
        if (request.name.isBlank()) {
            logger.warn("Intento de crear profesor con nombre en blanco")
            throw BlankNameException()
        }

        val professor = request.toEntity()
        val saved = professorRepository.save(professor)
        logger.info("Profesor creado exitosamente con ID: ${saved.id}")
        return saved.toResponse()
    }

    /**
     * Devuelve todos los profesores registrados.
     */
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Obteniendo la lista de todos los profesores")
        return professorRepository.findAll().map { it.toResponse() }
    }

    /**
     * Busca un profesor usando su identificador único.
     * 
     * @param id Identificador único del profesor.
     * @return El profesor encontrado.
     * @throws ProfessorNotFound Si no se encuentra el profesor.
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
     * Actualiza los datos de un profesor existente.
     * 
     * @param id Identificador único del profesor a actualizar.
     * @param request Nuevos datos del profesor.
     * @return El profesor actualizado.
     * @throws ProfessorNotFound Si no se encuentra el profesor.
     * @throws BlankNameException Si el nuevo nombre proporcionado está en blanco.
     */
    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        logger.info("Actualizando profesor con ID: $id")
        val professor = professorRepository.findById(id).orElseThrow {
            logger.warn("No se encontró el profesor para actualizar con ID: $id")
            throw ProfessorNotFound()
        }

        // Regla: El nombre no puede estar en blanco.
        if (request.name.isBlank()) {
            logger.warn("Intento de actualización con nombre en blanco para profesor con ID: $id")
            throw BlankNameException()
        }

        // Modificamos directamente las propiedades de la entidad gestionada por JPA/Hibernate.
        professor.name = request.name
        professor.email = request.email

        val saved = professorRepository.save(professor)
        logger.info("Profesor con ID: $id actualizado exitosamente")
        return saved.toResponse()
    }

    /**
     * Elimina a un profesor del sistema.
     * 
     * @param id Identificador único del profesor.
     * @throws ProfessorNotFound Si el profesor no existe.
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
