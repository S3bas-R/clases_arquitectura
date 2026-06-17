package com.pucetec.students.controllers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.services.ProfessorService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createProfessor(@RequestBody request: ProfessorRequest): ProfessorResponse {
        logger.info("Creating professor ${request.name}")
        return professorService.createProfessor(request)
    }

    @GetMapping
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        return professorService.getAllProfessors()
    }
}
