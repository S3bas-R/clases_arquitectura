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
import com.pucetec.students.exceptions.SubjectNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @Service: Sticker de Spring que indica que esta clase es el cerebro para controlar las materias del sistema.
 */
@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    /**
     * Crea una nueva materia en el sistema.
     * 
     * @param request Datos de la materia a crear.
     * @return La materia creada y mapeada a respuesta.
     * @throws BlankNameException Si el nombre o el código están en blanco.
     * @throws ProfessorNotFound Si el profesor asignado no existe.
     */
    fun createSubject(request: SubjectRequest): SubjectResponse {
        logger.info("Creando nueva materia: ${request.name} con código ${request.code}")

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
        logger.info("Materia creada con éxito. ID: ${saved.id}")
        return saved.toResponse()
    }

    /**
     * Devuelve todas las materias registradas.
     */
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Obteniendo todas las materias registradas")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    /**
     * Obtiene una materia por su identificador único.
     * 
     * @param id Identificador de la materia.
     * @return La materia correspondiente.
     * @throws SubjectNotFound Si la materia no existe.
     */
    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Buscando materia con ID: $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la materia con ID: $id")
            throw SubjectNotFound()
        }
        return subject.toResponse()
    }

    /**
     * Actualiza los datos de una materia existente.
     * 
     * @param id Identificador único de la materia a actualizar.
     * @param request Nuevos datos.
     * @return La materia actualizada.
     * @throws SubjectNotFound Si la materia no existe.
     * @throws BlankNameException Si el nombre o el código provistos están vacíos.
     * @throws ProfessorNotFound Si el nuevo profesor asignado no existe.
     */
    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        logger.info("Actualizando materia con ID: $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            logger.warn("No se encontró la materia para actualizar con ID: $id")
            throw SubjectNotFound()
        }

        // Reglas de validación.
        if (request.name.isBlank()) {
            logger.warn("Intento de actualizar materia con nombre vacío")
            throw BlankNameException()
        }
        if (request.code.isBlank()) {
            logger.warn("Intento de actualizar materia con código vacío")
            throw BlankNameException()
        }

        // Buscamos si el nuevo profesor asignado existe.
        val professor = professorRepository.findById(request.professorId).orElseThrow {
            logger.warn("No se encontró el profesor con ID: ${request.professorId}")
            throw ProfessorNotFound()
        }

        // Modificamos directamente las propiedades de la entidad gestionada por JPA.
        subject.name = request.name
        subject.code = request.code
        subject.description = request.description
        subject.professor = professor

        val saved = subjectRepository.save(subject)
        logger.info("Materia con ID: $id actualizada exitosamente")
        return saved.toResponse()
    }

    /**
     * Elimina una materia de la base de datos.
     * 
     * @param id Identificador único de la materia.
     * @throws SubjectNotFound Si la materia no existe.
     */
    fun deleteSubject(id: Long) {
        logger.info("Eliminando materia con ID: $id")
        if (!subjectRepository.existsById(id)) {
            logger.warn("No se encontró la materia para eliminar con ID: $id")
            throw SubjectNotFound()
        }
        subjectRepository.deleteById(id)
        logger.info("Materia con ID: $id eliminada correctamente")
    }
}
