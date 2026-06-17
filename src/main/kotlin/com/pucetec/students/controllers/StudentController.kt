package com.pucetec.students.controllers

// Importamos lo que necesitamos de otras carpetas
import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

// @RestController: Sticker que dice que esta clase es el "Mesero" de la API.
@RestController
// @RequestMapping: Dice que todas las órdenes para esta clase empiezan con "/api/students".
@RequestMapping("/api/students")
class StudentController (
    // Recibe el "Cerebro" (Service) para mandarle los pedidos de cocina.
    val studentService: StudentService
){
    // Creamos el diario (logger) para anotar qué órdenes van llegando.
    val logger = LoggerFactory.getLogger(javaClass)

    // @PostMapping: Se activa cuando el cliente quiere CREAR (enviar datos nuevos).
    @PostMapping
    fun createStudent(
        // @RequestBody: Indica que los datos vienen en el "cuerpo" del paquete (Request).
        @RequestBody request: StudentRequest
    ) : StudentResponse {
        logger.info("Creating student ${request.name}")
        // Le pasa el pedido al Service y devuelve lo que el Service responda.
        return studentService.createStudent(request)
    }

    // @GetMapping: Se activa cuando el cliente quiere PEDIR (ver datos existentes).
    @GetMapping
    fun getAllStudents() : List<StudentResponse> {
        logger.info("Getting all students")
        // Pide la lista al Service y la entrega al cliente.
        return studentService.getAllStudents()
    }

    // @PutMapping: Se activa cuando el cliente quiere ACTUALIZAR (cambiar algo que ya existe).
    // "/{id}": Significa que en la dirección vendrá el número del estudiante (ej: /api/students/5).
    @PutMapping("/{id}")
    fun updateStudent(
        // @PathVariable: Saca el número de ID que viene en la dirección URL.
        @PathVariable id: Long,
        // Recibe los nuevos datos en el cuerpo del mensaje.
        @RequestBody student: StudentRequest
    ): StudentResponse{
        // Le dice al Service: "Actualiza al estudiante número X con estos datos".
        return studentService.updateStudent(id, student)
    }

    // @DeleteMapping: Se activa cuando el cliente quiere BORRAR.
    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id: Long){
        // Le dice al Service que borre al estudiante con ese número.
        studentService.deleteStudent(id)
    }
}
