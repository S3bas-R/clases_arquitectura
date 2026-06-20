package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// @RestController: Dice que esta clase maneja las peticiones web (HTTP) de las materias.
@RestController
@RequestMapping("/api/subjects")
class SubjectController(
    private val subjectService: SubjectService
) {
    private val logger = LoggerFactory.getLogger(SubjectController::class.java)

    // @PostMapping: Crea una nueva materia. Responde con 201 (Creado).
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubject(@RequestBody request: SubjectRequest): SubjectResponse {
        logger.info("Recibida petición para crear una materia: ${request.name}")
        return subjectService.createSubject(request)
    }

    // @GetMapping: Obtiene la lista de todas las materias. Responde con 200 (OK).
    @GetMapping
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Recibida petición para listar todas las materias")
        return subjectService.getAllSubjects()
    }

    // @GetMapping("/{id}"): Obtiene una materia por su ID único. Responde con 200 (OK).
    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): SubjectResponse {
        logger.info("Recibida petición para obtener materia con ID: $id")
        return subjectService.getSubjectById(id)
    }

    // @PutMapping("/{id}"): Actualiza los datos de una materia. Responde con 200 (OK).
    @PutMapping("/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        logger.info("Recibida petición para actualizar materia con ID: $id")
        return subjectService.updateSubject(id, request)
    }

    // @DeleteMapping("/{id}"): Elimina una materia por su ID. Responde con 204 (Sin contenido).
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubject(@PathVariable id: Long) {
        logger.info("Recibida petición para eliminar materia con ID: $id")
        subjectService.deleteSubject(id)
    }
}
