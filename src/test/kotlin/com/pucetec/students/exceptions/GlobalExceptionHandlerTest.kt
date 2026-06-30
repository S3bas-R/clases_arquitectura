package com.pucetec.students.exceptions

import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Clase de pruebas unitarias para GlobalExceptionHandler.
 * Llama directamente a los métodos del controlador de excepciones global para lograr una cobertura del 100% de líneas y ramas.
 * Todos los comentarios están detallados en español.
 */
class GlobalExceptionHandlerTest {

    private val exceptionHandler = GlobalExceptionHandler()

    /**
     * Prueba que handleStudentNotFound retorne HttpStatus.NOT_FOUND y el mensaje de la excepción correspondiente.
     */
    @Test
    fun `handleStudentNotFound debe retornar HttpStatus NOT_FOUND con el mensaje de StudentNotFoundException`() {
        // Arrange
        val exception = StudentNotFoundException()

        // Act
        val response = exceptionHandler.handleStudentNotFound(exception)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Estudiante no encontrado", response.body)
    }

    /**
     * Prueba que handleProfessorNotFound retorne HttpStatus.NOT_FOUND y el mensaje de la excepción correspondiente.
     */
    @Test
    fun `handleProfessorNotFound debe retornar HttpStatus NOT_FOUND con el mensaje de ProfessorNotFound`() {
        // Arrange
        val exception = ProfessorNotFound()

        // Act
        val response = exceptionHandler.handleProfessorNotFound(exception)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Profesor no encontrado", response.body)
    }

    /**
     * Prueba que handleSubjectNotFound retorne HttpStatus.NOT_FOUND y el mensaje de la excepción correspondiente.
     */
    @Test
    fun `handleSubjectNotFound debe retornar HttpStatus NOT_FOUND con el mensaje de SubjectNotFound`() {
        // Arrange
        val exception = SubjectNotFound()

        // Act
        val response = exceptionHandler.handleSubjectNotFound(exception)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Materia no encontrada", response.body)
    }

    /**
     * Prueba que handleEnrollmentNotFound retorne HttpStatus.NOT_FOUND y el mensaje de la excepción correspondiente.
     */
    @Test
    fun `handleEnrollmentNotFound debe retornar HttpStatus NOT_FOUND con el mensaje de EnrollmentNotFound`() {
        // Arrange
        val exception = EnrollmentNotFound()

        // Act
        val response = exceptionHandler.handleEnrollmentNotFound(exception)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Inscripción no encontrada", response.body)
    }

    /**
     * Prueba que handleBlankName retorne HttpStatus.BAD_REQUEST y el mensaje de la excepción correspondiente.
     */
    @Test
    fun `handleBlankName debe retornar HttpStatus BAD_REQUEST con el mensaje de BlankNameException`() {
        // Arrange
        val exception = BlankNameException()

        // Act
        val response = exceptionHandler.handleBlankName(exception)

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("El nombre no puede estar vacío", response.body)
    }

    /**
     * Prueba que handleDataIntegrityViolation retorne HttpStatus.CONFLICT y el mensaje explicativo adecuado.
     */
    @Test
    fun `handleDataIntegrityViolation debe retornar HttpStatus CONFLICT con mensaje explicativo`() {
        // Arrange
        val exception = DataIntegrityViolationException("Error de restricción de clave foránea")

        // Act
        val response = exceptionHandler.handleDataIntegrityViolation(exception)

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertNotNull(response.body)
        assert(response.body!!.contains("restricciones en la base de datos"))
    }

    /**
     * Prueba que handleAllUncaughtExceptions retorne HttpStatus.INTERNAL_SERVER_ERROR y un mensaje genérico seguro.
     */
    @Test
    fun `handleAllUncaughtExceptions debe retornar HttpStatus INTERNAL_SERVER_ERROR con mensaje generico seguro`() {
        // Arrange
        val exception = RuntimeException("Error fatal no controlado")

        // Act
        val response = exceptionHandler.handleAllUncaughtExceptions(exception)

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertNotNull(response.body)
        assert(response.body!!.contains("Ocurrió un error inesperado"))
    }
}
