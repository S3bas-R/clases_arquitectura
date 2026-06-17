package com.pucetec.students.dto

// DTO (Data Transfer Object): Son las "Cajas de Envío" para mover datos.

/**
 * StudentRequest: La caja que llega desde afuera cuando alguien quiere guardar un alumno.
 * Usamos 'data class' porque es una clase hecha solo para contener datos.
 */
data class StudentRequest(
    val name: String,         // El nombre es obligatorio (String).
    val email: String? = null // El email es opcional (String?). Si no lo envían, es null (vacío).
)

/**
 * StudentResponse: La caja que el sistema envía hacia afuera para mostrar un alumno.
 */
data class StudentResponse(
    val id: Long,        // Incluimos el ID porque el cliente necesita saber qué número le tocó.
    val name: String,
    val email: String?   // El email puede ser nulo si no tenía uno guardado.
)
