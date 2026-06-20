package com.pucetec.students.exceptions

/**
 * EnrollmentNotFound: Excepción lanzada cuando una inscripción no se encuentra en el sistema.
 * Debe llamarse exactamente así según la especificación del deber.md.
 */
class EnrollmentNotFound : RuntimeException("Inscripción no encontrada")
