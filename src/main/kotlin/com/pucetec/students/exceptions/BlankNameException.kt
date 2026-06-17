package com.pucetec.students.exceptions

/**
 * BlankNameException: Un "Grito de Error" personalizado.
 * Se lanza cuando alguien intenta guardar algo con el nombre vacío.
 * Hereda de RuntimeException para poder detener el programa.
 */
class BlankNameException : RuntimeException("El nombre no puede estar vacío")
