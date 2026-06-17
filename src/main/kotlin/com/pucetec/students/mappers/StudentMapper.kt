package com.pucetec.students.mappers

// Importamos las cajas (DTOs) y el archivo oficial (Entity).
import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student

// El Mapper es el TRADUCTOR del sistema.

/**
 * toEntity: Convierte la caja de llegada (Request) en un archivo oficial (Entity).
 * Se usa para poder GUARDAR en la base de datos.
 */
fun StudentRequest.toEntity() = Student(
    name = this.name,   // Copia el nombre de la caja al archivo.
    email = this.email  // Copia el email de la caja al archivo.
)

/**
 * toResponse: Convierte el archivo oficial (Entity) en una caja de salida (Response).
 * Se usa para MOSTRAR los datos al cliente de forma bonita.
 */
fun Student.toResponse() = StudentResponse(
    id = this.id,       // Pone el ID en la caja de salida.
    name = this.name,   // Pone el nombre en la caja de salida.
    email = this.email  // Pone el email en la caja de salida.
)
