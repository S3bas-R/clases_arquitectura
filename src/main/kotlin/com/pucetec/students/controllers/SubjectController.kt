package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(
    private val subjectService: SubjectService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createSubject(@RequestBody request: SubjectRequest): SubjectResponse {
        logger.info("Creating subject ${request.name}")
        return subjectService.createSubject(request)
    }

    @GetMapping
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectService.getAllSubjects()
    }
}
