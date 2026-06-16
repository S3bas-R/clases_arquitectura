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
class StudentController (
    val studentService: StudentService
){
    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/api/students")
    fun createStudent(
        @RequestBody
        request: StudentRequest
        ) : StudentResponse {
        logger.info("Creating student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping ("/api/students")
    fun getAllStudents() : List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }

    @PutMapping("/api/students")
    fun updateStudent(
        id: Long,
        student: StudentRequest
    ): StudentResponse{
        return studentService.updateStudent(id, student)
    }

    @DeleteMapping("/api/students")
    fun deleteStudent(){

    }


}



//que es un ORM?
