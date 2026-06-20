package com.pucetec.students.exceptions

/**
 * SubjectNotFound: Excepción lanzada cuando una materia no se encuentra en el sistema.
 * Debe llamarse exactamente así según la especificación del deber.md.
 */
class SubjectNotFound : RuntimeException("Materia no encontrada")
