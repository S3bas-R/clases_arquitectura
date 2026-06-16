package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student
import com.pucetec.students.repositories.StudentsRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService (
    private val repository: StudentsRepository
){
    val logger = LoggerFactory.getLogger(javaClass)

    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating Student ${request.name}")

        // Validar
        // TODO: Validar que el estudiante no exista previamente

        // Crear entidad
        val StudentToSave = Student(
            name = request.name,
            email = request.email,
        )

        // Guardar entidad
        val savedStudent = repository.save(StudentToSave)
        logger.info("saved student with id ${savedStudent.id}")
        return StudentResponse(
            id = savedStudent.id,
            name = savedStudent.name,
            email = savedStudent.email,
        )


    }

    fun getAllStudents() : List<StudentResponse> {
        logger.info("Get all Student list")

        // COnsultar todos los estudiantes
        val students: List <Student> = repository.findAll()

        return students.map{ miEstudiante: Student ->
            StudentResponse(
                id = miEstudiante.id,
                name = miEstudiante.name,
                email = miEstudiante.email
                )
        }
    }

    fun updateStudent(id: Long,student: StudentRequest): StudentResponse {
        //Logica de actualizacion
        return StudentResponse(
            id = 1,
            name = student.name,
            email = student.email,
        )
    }
}