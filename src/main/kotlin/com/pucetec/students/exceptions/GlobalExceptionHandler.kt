package com.pucetec.students.exceptions

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * @ControllerAdvice: Este es el sticker del "Paramédico Global".
 * Esta clase está vigilando todo el sistema de forma activa. Si algún Controller lanza
 * una excepción (Exception), esta clase intercepta el error y genera una respuesta limpia
 * y controlada para el cliente, evitando que la aplicación falle con un error 500 no controlado.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    // Registrador de logs para registrar errores inesperados en el servidor.
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * @ExceptionHandler: Le dice al paramédico qué excepción específica debe atender.
     * En este caso, atiende cuando no se encuentra un estudiante (StudentNotFoundException).
     * Retorna un código HTTP 404 (NOT FOUND).
     */
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(ex: StudentNotFoundException): ResponseEntity<String> {
        logger.warn("Excepción detectada: StudentNotFoundException - Mensaje: ${ex.message}")
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando no se encuentra un profesor (ProfessorNotFound).
     * Retorna un código HTTP 404 (NOT FOUND).
     */
    @ExceptionHandler(ProfessorNotFound::class)
    fun handleProfessorNotFound(ex: ProfessorNotFound): ResponseEntity<String> {
        logger.warn("Excepción detectada: ProfessorNotFound - Mensaje: ${ex.message}")
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando no se encuentra una materia (SubjectNotFound).
     * Retorna un código HTTP 404 (NOT FOUND).
     */
    @ExceptionHandler(SubjectNotFound::class)
    fun handleSubjectNotFound(ex: SubjectNotFound): ResponseEntity<String> {
        logger.warn("Excepción detectada: SubjectNotFound - Mensaje: ${ex.message}")
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando no se encuentra una inscripción (EnrollmentNotFound).
     * Retorna un código HTTP 404 (NOT FOUND).
     */
    @ExceptionHandler(EnrollmentNotFound::class)
    fun handleEnrollmentNotFound(ex: EnrollmentNotFound): ResponseEntity<String> {
        logger.warn("Excepción detectada: EnrollmentNotFound - Mensaje: ${ex.message}")
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    /**
     * Atiende cuando se envía un campo obligatorio vacío o en blanco (BlankNameException).
     * Retorna un código HTTP 400 (BAD REQUEST).
     */
    @ExceptionHandler(BlankNameException::class)
    fun handleBlankName(ex: BlankNameException): ResponseEntity<String> {
        logger.warn("Excepción de validación detectada: BlankNameException - Mensaje: ${ex.message}")
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    /**
     * Atiende violaciones de restricciones de integridad de base de datos (DataIntegrityViolationException).
     * Esto sucede, por ejemplo, cuando se intenta borrar una entidad que tiene registros hijos o dependencias
     * (por ejemplo, borrar un Profesor con materias asignadas, o un Estudiante con inscripciones),
     * o cuando hay violaciones de unicidad.
     * Retorna un código HTTP 409 (CONFLICT) y previene errores 500 no controlados de base de datos.
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<String> {
        logger.error("Violación de integridad de datos detectada en la base de datos", ex)
        val mensajeRespuesta = "No se puede realizar la operación debido a una violación de restricciones en la base de datos " +
                "(ej: el registro está asociado a otras entidades o infringe una restricción de clave foránea o de unicidad)."
        return ResponseEntity(mensajeRespuesta, HttpStatus.CONFLICT)
    }

    /**
     * Atiende CUALQUIER otra excepción no controlada en el sistema (catch-all).
     * Esto asegura que bajo ninguna circunstancia el usuario reciba un error 500 no controlado
     * de Spring con un stacktrace expuesto. En su lugar, se devuelve una respuesta controlada y estructurada
     * con un código HTTP 500 (INTERNAL SERVER ERROR) y un mensaje seguro.
     */
    @ExceptionHandler(Exception::class)
    fun handleAllUncaughtExceptions(ex: Exception): ResponseEntity<String> {
        logger.error("Excepción interna del servidor no controlada: ", ex)
        return ResponseEntity(
            "Ocurrió un error inesperado en el servidor. Por favor, comuníquese con soporte técnico.",
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
