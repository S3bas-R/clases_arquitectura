package com.pucetec.students.entities

// Importamos herramientas para hablar con la base de datos (JPA).
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

// @Entity: Sticker que dice que esta clase es una TABLA en la base de datos.
@Entity
// @Table: Le pone el nombre real a la tabla en la base de datos.
@Table(name = "students")
data class Student (
    // @Id: Dice que este campo es la "Cédula" o llave única del estudiante.
    @Id
    // @GeneratedValue: Dice que el sistema le pondrá el número automáticamente (1, 2, 3...).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    
    // 'var' porque el nombre y el email pueden cambiar después (Actualizar).
    var name: String = "",
    var email: String? = null,
)
