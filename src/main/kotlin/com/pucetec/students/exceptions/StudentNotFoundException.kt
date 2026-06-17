package com.pucetec.students.exceptions

// Esta clase es un "Grito de Error" personalizado.
// Hereda de 'RuntimeException', que es el grito base de Java/Kotlin.
class StudentNotFoundException : RuntimeException("Estudiante no encontrado")
// Cuando el sistema lanza esto, se detiene y avisa que no halló al alumno.
