package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// @Service: Sticker que le dice a Spring que esta clase es el cerebro para los estudiantes.
@Service
class StudentService(
    // Inyectamos el repositorio (el bibliotecario) para hablar con la base de datos.
    private val studentRepository: StudentRepository
) {
    // El diario de la clase para escribir notas en la consola.
    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    /**
     * Guarda un nuevo estudiante en la base de datos.
     * 
     * @param request La cajita de entrada con el nombre y email del estudiante.
     * @return La cajita de salida del estudiante creado con su ID.
     * @throws BlankNameException Si el nombre del estudiante está vacío.
     */
    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Iniciando la creación del estudiante: ${request.name}")

        // Regla: El nombre no puede ser un espacio vacío.
        if (request.name.isBlank()) {
            logger.warn("El nombre del estudiante está en blanco")
            throw BlankNameException()
        }

        // Traducimos a entidad y la guardamos en la base de datos.
        val studentEntity = request.toEntity()
        val savedStudent = studentRepository.save(studentEntity)
        logger.info("Estudiante guardado exitosamente con ID: ${savedStudent.id}")

        // Retornamos el formato bonito.
        return savedStudent.toResponse()
    }

    /**
     * Retorna la lista de todos los estudiantes registrados.
     */
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Obteniendo la lista de todos los estudiantes")
        val savedStudents = studentRepository.findAll()
        return savedStudents.map { it.toResponse() }
    }

    /**
     * Busca un estudiante en la base de datos por su identificador único.
     * 
     * @param id El identificador único del estudiante.
     * @return El estudiante encontrado formateado como DTO de salida.
     * @throws StudentNotFoundException Si el estudiante no existe.
     */
    fun getStudentById(id: Long): StudentResponse {
        logger.info("Buscando estudiante con ID: $id")
        val student = studentRepository.findById(id).orElseThrow {
            logger.warn("No se encontró el estudiante con ID: $id")
            throw StudentNotFoundException()
        }
        return student.toResponse()
    }

    /**
     * Actualiza los datos de un estudiante que ya existe.
     * 
     * @param id El identificador único del estudiante.
     * @param request Los nuevos datos (nombre y email).
     * @return El estudiante actualizado.
     */
    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        logger.info("Actualizando estudiante con ID: $id")
        val student = studentRepository.findById(id).orElseThrow {
            logger.warn("No se encontró el estudiante para actualizar con ID: $id")
            throw StudentNotFoundException()
        }

        // Validamos que el nuevo nombre no esté vacío.
        if (request.name.isBlank()) {
            logger.warn("El nombre proporcionado para la actualización está en blanco")
            throw BlankNameException()
        }

        // Copiamos los valores nuevos.
        student.name = request.name
        student.email = request.email

        val updatedStudent = studentRepository.save(student)
        logger.info("Estudiante con ID: $id actualizado correctamente")
        return updatedStudent.toResponse()
    }

    /**
     * Borra un estudiante del sistema usando su ID.
     * 
     * @param id El identificador único del estudiante.
     */
    fun deleteStudent(id: Long) {
        logger.info("Eliminando estudiante con ID: $id")
        // Verificamos si existe antes de borrarlo.
        if (!studentRepository.existsById(id)) {
            logger.warn("No se encontró el estudiante para eliminar con ID: $id")
            throw StudentNotFoundException()
        }
        studentRepository.deleteById(id)
        logger.info("Estudiante con ID: $id eliminado correctamente")
    }
}
