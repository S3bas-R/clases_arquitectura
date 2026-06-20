package com.pucetec.students.controllers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.services.ProfessorService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// @RestController: Dice que esta clase maneja las peticiones web (HTTP) de los profesores.
@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService
) {
    private val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    // @PostMapping: Crea un nuevo profesor. Responde con 201 (Creado).
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessor(@RequestBody request: ProfessorRequest): ProfessorResponse {
        logger.info("Recibida petición para crear un profesor: ${request.name}")
        return professorService.createProfessor(request)
    }

    // @GetMapping: Obtiene la lista de todos los profesores. Responde con 200 (OK).
    @GetMapping
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Recibida petición para listar todos los profesores")
        return professorService.getAllProfessors()
    }

    // @GetMapping("/{id}"): Obtiene un profesor por su ID único. Responde con 200 (OK).
    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ProfessorResponse {
        logger.info("Recibida petición para obtener profesor con ID: $id")
        return professorService.getProfessorById(id)
    }

    // @PutMapping("/{id}"): Actualiza los datos del profesor. Responde con 200 (OK).
    @PutMapping("/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {
        logger.info("Recibida petición para actualizar profesor con ID: $id")
        return professorService.updateProfessor(id, request)
    }

    // @DeleteMapping("/{id}"): Elimina un profesor por su ID. Responde con 24 (Sin contenido).
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfessor(@PathVariable id: Long) {
        logger.info("Recibida petición para eliminar profesor con ID: $id")
        professorService.deleteProfessor(id)
    }
}
