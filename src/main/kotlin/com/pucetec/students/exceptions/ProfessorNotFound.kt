package com.pucetec.students.exceptions

/**
 * ProfessorNotFound: Excepción lanzada cuando un profesor no se encuentra en el sistema.
 * Debe llamarse exactamente así según la especificación del deber.md.
 */
class ProfessorNotFound : RuntimeException("Profesor no encontrado")
