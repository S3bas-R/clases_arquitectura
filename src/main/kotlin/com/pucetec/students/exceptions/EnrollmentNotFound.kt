package com.pucetec.students.exceptions

/**
 * Excepción personalizada que representa un error cuando una inscripción
 * no es encontrada en el sistema.
 */
class EnrollmentNotFound : RuntimeException("Inscripción no encontrada")
