package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {
    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        val professor = request.toEntity()
        return professorRepository.save(professor).toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        return professorRepository.findAll().map { it.toResponse() }
    }
}
