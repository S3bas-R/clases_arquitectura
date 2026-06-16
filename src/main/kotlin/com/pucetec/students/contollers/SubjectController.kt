package com.pucetec.students.contollers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController (
    val subjetctService: SubjectService
){
    @PostMapping("/api/subjects")
    fun createSubject(
        @RequestBody
        request: SubjectRequest) {}
}



//que es un ORM?
