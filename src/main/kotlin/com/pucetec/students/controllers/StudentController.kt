package com.pucetec.students.controllers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// @RestController: Dice que esta clase maneja las peticiones web (HTTP) de los estudiantes.
@RestController
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService
) {
    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    // @PostMapping: Se activa al enviar una petición POST para crear un estudiante.
    // @ResponseStatus(HttpStatus.CREATED): Indica que responde con el código 201 (Creado exitosamente).
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(@RequestBody request: StudentRequest): StudentResponse {
        logger.info("Recibida petición para crear un estudiante: ${request.name}")
        return studentService.createStudent(request)
    }

    // @GetMapping: Se activa al enviar una petición GET para obtener todos los estudiantes.
    @GetMapping
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Recibida petición para listar todos los estudiantes")
        return studentService.getAllStudents()
    }

    // @GetMapping("/{id}"): Se activa al enviar una petición GET con el identificador en la URL (ej: /api/students/1).
    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): StudentResponse {
        logger.info("Recibida petición para obtener el estudiante con ID: $id")
        return studentService.getStudentById(id)
    }

    // @PutMapping("/{id}"): Se activa al enviar una petición PUT para actualizar datos de un estudiante.
    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): StudentResponse {
        logger.info("Recibida petición para actualizar el estudiante con ID: $id")
        return studentService.updateStudent(id, request)
    }

    // @DeleteMapping("/{id}"): Se activa al enviar una petición DELETE para borrar a un estudiante.
    // @ResponseStatus(HttpStatus.NO_CONTENT): Indica que responde con el código 204 (Sin contenido, borrado con éxito).
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: Long) {
        logger.info("Recibida petición para eliminar el estudiante con ID: $id")
        studentService.deleteStudent(id)
    }
}
