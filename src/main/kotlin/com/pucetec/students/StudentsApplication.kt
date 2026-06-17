package com.pucetec.students

// Importamos las herramientas de Spring Boot para arrancar la aplicación.
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @SpringBootApplication: Este es el STICKER MÁS PODEROSO. 
 * Le dice a la computadora: "¡Ey! Este es el motor de mi proyecto".
 * Cuando este sticker está puesto, Spring busca automáticamente todos los
 * @Service, @RestController, @Repository, etc., y los conecta entre sí.
 */
@SpringBootApplication
class StudentsApplication

/**
 * fun main: Es el botón de ENCENDIDO del sistema. 
 * Todo programa de computadora necesita un lugar exacto por donde empezar a correr.
 */
fun main(args: Array<String>) {
    // runApplication: Es el comando que gira la llave y arranca el motor de Spring.
	runApplication<StudentsApplication>(*args)
}

/*
 * -------------------------------------------------------------------------
 * 🏗️ GUÍA PARA CREAR UN NUEVO MÓDULO (Ej: Estudiantes) 🏗️
 * -------------------------------------------------------------------------
 * Si vas a crear algo nuevo, este es el orden recomendado (de adentro hacia afuera):
 *
 * 1. ENTITY (La Ficha): Crea primero el molde de cómo se guardará en la base de datos.
 *    (Ej: Student.kt)
 *
 * 2. REPOSITORY (El Bibliotecario): Crea la interface para que ya puedas guardar la ficha.
 *    (Ej: StudentsRepository.kt)
 *
 * 3. DTO (Las Cajas): Define qué datos vas a recibir y cuáles vas a mostrar.
 *    (Ej: StudentRequest y StudentResponse en StudentDto.kt)
 *
 * 4. MAPPER (El Traductor): Crea las funciones para convertir cajas en fichas y viceversa.
 *    (Ej: StudentMapper.kt)
 *
 * 5. SERVICE (El Cerebro): Escribe la lógica. Aquí conectas al Bibliotecario con el Traductor.
 *    (Ej: StudentService.kt)
 *
 * 6. CONTROLLER (El Mesero): Finalmente, crea la puerta de entrada para que el usuario 
 *    pueda pedir cosas al Cerebro.
 *    (Ej: StudentController.kt)
 *
 * 🔗 ¿Cómo se conectan?
 * El Mesero -> llama al -> Cerebro -> usa al -> Traductor y al Bibliotecario -> manejan la -> Ficha.
 * -------------------------------------------------------------------------
 */
