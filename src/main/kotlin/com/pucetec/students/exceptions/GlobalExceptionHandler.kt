package com.pucetec.students.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * @ControllerAdvice: Este es el sticker del "Paramédico Global".
 * Esta clase está vigilando todo el sistema. Si algún Controller lanza un grito de auxilio
 * (Exception), el paramédico corre a ayudar.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * @ExceptionHandler: Le dice al paramédico EXACTAMENTE qué grito debe atender.
     * En este caso, atiende cuando no se encuentra un estudiante.
     */
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(ex: StudentNotFoundException): ResponseEntity<String> {
        // ResponseEntity: Es la caja de respuesta final.
        // ex.message: El mensaje de error que definimos (ej: "Estudiante no encontrado").
        // HttpStatus.NOT_FOUND: El código de internet 404 (No encontrado).
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando no se encuentra un profesor.
     */
    @ExceptionHandler(ProfessorNotFound::class)
    fun handleProfessorNotFound(ex: ProfessorNotFound): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando no se encuentra una materia.
     */
    @ExceptionHandler(SubjetctNotFound::class)
    fun handleSubjectNotFound(ex: SubjetctNotFound): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando alguien envía un nombre en blanco.
     */
    @ExceptionHandler(BlankNameException::class)
    fun handleBlankName(ex: BlankNameException): ResponseEntity<String> {
        // HttpStatus.BAD_REQUEST: El código de internet 400 (Petición incorrecta).
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    /**
     * Atiende cuando no se encuentra una inscripción.
     */
    @ExceptionHandler(EnrollmentNotFound::class)
    fun handleEnrollmentNotFound(ex: EnrollmentNotFound): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }
}
