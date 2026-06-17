package com.pucetec.students.services

// Importamos otras partes del proyecto y herramientas de Spring
import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.StudentsRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// @Service: Esta anotación le dice a Spring que esta clase es el "Cerebro" (Lógica de negocio).
// Spring la crea automáticamente para que podamos usarla en otros lados.
@Service
class StudentService(
    // Inyectamos el repositorio (el bibliotecario) para hablar con la base de datos.
    // Usamos 'private val' porque solo este servicio debe usarlo y no va a cambiar.
    private val repository: StudentsRepository
) {
    // LoggerFactory.getLogger: Crea un objeto para escribir en el "diario" (consola).
    // javaClass: Le dice al logger que los mensajes vienen exactamente de ESTA clase.
    private val logger = LoggerFactory.getLogger(javaClass)

    // fun: Palabra para crear una función (una acción).
    // createStudent: Nombre de la acción.
    // request: La "caja" de datos que recibimos desde afuera.
    // : StudentResponse: Indica que al final devolveremos una "caja" de respuesta.
    fun createStudent(request: StudentRequest): StudentResponse {
        // logger.info: Escribe un mensaje en la consola. El '$' mete el nombre dentro del texto.
        logger.info("Creating Student ${request.name}")
        
        // toEntity(): Traduce los datos que recibimos a un formato que la base de datos entiende.
        val studentToSave = request.toEntity()
        
        // repository.save: Le pide al bibliotecario que guarde el estudiante en la estantería (BD).
        val savedStudent = repository.save(studentToSave)
        
        // return: Entrega el resultado final hacia afuera de la función.
        // toResponse(): Traduce el estudiante guardado a un formato bonito para el usuario.
        return savedStudent.toResponse()
    }

    // List<StudentResponse>: Significa que devolveremos una LISTA de cajas de respuesta.
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        
        // repository.findAll(): Busca a TODOS los estudiantes en la base de datos.
        // .map { ... }: "Mapear" significa transformar cada elemento de una lista.
        // Aquí, tomamos cada Estudiante real y lo transformamos en un StudentResponse (bonito).
        return repository.findAll().map { it.toResponse() }
    }

    // id: Long: Recibimos el número único del estudiante que queremos cambiar.
    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        logger.info("Updating student with id $id")
        
        // repository.findById(id): Busca al estudiante por su número de cédula (ID).
        // .orElseThrow(): Si no lo encuentra, lanza un "grito de error" (Excepción).
        val student = repository.findById(id).orElseThrow()
        
        // .copy: Crea una copia del estudiante pero con los datos nuevos que enviamos.
        val updatedStudent = student.copy(
            name = request.name,
            email = request.email
        )
        
        // Guardamos los cambios y devolvemos la respuesta traducida.
        return repository.save(updatedStudent).toResponse()
    }

    // Esta función no devuelve nada (por eso no tiene ': Algo' al final).
    fun deleteStudent(id: Long) {
        logger.info("Deleting student with id $id")
        
        // Le pide al bibliotecario que borre el registro con ese ID.
        repository.deleteById(id)
    }
}
