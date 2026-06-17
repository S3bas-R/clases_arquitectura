# 📚 Guía Maestra: ¿Cómo funciona este sistema? (Manual Completo)

Esta guía es un viaje desde los conceptos más básicos (los ladrillos) hasta la arquitectura compleja de una fábrica de datos. Está diseñada para que alguien que no sabe nada de programación pueda entenderlo todo.

---

## 🧱 Fase 1: Los Ladrillos (Conceptos de Kotlin)

Antes de construir, necesitamos conocer los materiales.

### 1.1 ¿Qué es una Clase (`class`)?
Es un **molde**. Si quieres fabricar autos, primero necesitas el plano que diga que todos los autos tienen 4 ruedas y un motor.
*   **En el código**: `class StudentService` es el molde que contiene la "inteligencia" para manejar estudiantes.

### 1.2 ¿Qué es una `data class`? (La caja de datos)
Es un molde especial hecho **solo para guardar cosas**, no para pensar. Kotlin le regala funciones automáticas para copiar y comparar datos.
*   **Ejemplo**: `data class StudentRequest(val name: String)` es una cajita que solo guarda el nombre.

### 1.3 `val` vs `var` (Cajitas con y sin candado)
*   **`val` (Valor)**: Le pones un dato y **se le pone candado**. No puede cambiar. Es seguro.
    *   `val id = 1` (El ID nunca cambiará).
*   **`var` (Variable)**: Es una caja abierta. Puedes quitar lo que hay y poner algo nuevo.
    *   `var name = "Juan"` (Luego puedes cambiarlo a "Pepe").

### 1.4 Tipos de Datos (¿Qué cabe en la caja?)
*   **`String`**: Texto (letras). Ejemplo: `"Hola Mundo"`.
*   **`Long`**: Números enteros muy grandes. Se usa para los IDs (1, 2, 3...).
*   **`String?` (Nulidad)**: El signo `?` significa que la caja puede estar **vacía** (`null`). Es como decir: "Este dato es opcional".

### 1.5 ¿Qué es una `interface`? (El Contrato)
Es un documento que dice **qué** se debe hacer, pero no **cómo**.
*   **Ejemplo**: Un contrato de trabajo que dice "Debes limpiar la oficina". No te dice si usar escoba o aspiradora, solo que debe quedar limpio.
*   En el sistema, el **Repository** es una interface porque nosotros solo decimos "quiero guardar", y Spring Boot (nuestra herramienta) se encarga de saber cómo se hace técnicamente.

---

## 🌍 Fase 2: El Mundo Exterior (La API)

### 2.1 ¿Qué es una API? (La metáfora del restaurante)
*   **Cliente (Tú)**: Quieres información.
*   **Cocina (Sistema)**: Donde están los datos.
*   **Mesero (API)**: El intermediario. Tú le das una orden, él va a la cocina y vuelve con la respuesta.

### 2.2 Endpoint (La dirección)
Es la URL o puerta de entrada. Ejemplo: `http://tusistema.com/api/students`.

### 2.3 Request (Petición) y Response (Respuesta)
*   **Request**: Lo que tú envías (ej: nombre del alumno).
*   **Response**: Lo que el sistema te devuelve (ej: "Alumno guardado con éxito").

---

## 🏗️ Fase 3: La Fábrica (Arquitectura y Ejemplos)

Cuando un dato llega al sistema, pasa por estas estaciones en este orden:

### Estación 1: El DTO (Data Transfer Object)
Es la "caja de viaje". No es el archivo oficial, es solo para mover datos.
*   **Ejemplo en `StudentDto.kt`**:
    ```kotlin
    data class StudentRequest(
        val name: String,   // Dato que recibimos
        val email: String?  // Dato opcional
    )
    ```

### Estación 2: El Controller (El Recepcionista)
Es el que recibe la Request. Usa la anotación `@RestController` (un sticker que le da el poder de escuchar internet).
*   **Ejemplo en `StudentController.kt`**:
    ```kotlin
    @PostMapping // Significa: "Escucha cuando alguien quiera CREAR algo"
    fun createStudent(@RequestBody request: StudentRequest): StudentResponse {
        logger.info("Recibí a: ${request.name}") // El Logger anota en su diario
        return studentService.createStudent(request) // Le pasa el trabajo al cerebro
    }
    ```

### Estación 3: El Service (El Cerebro)
Aquí está la lógica. Decide qué hacer. Usa `@Service`.
*   **Ejemplo en `StudentService.kt`**:
    ```kotlin
    fun createStudent(request: StudentRequest): StudentResponse {
        val entity = request.toEntity() // Llama al traductor (Mapper)
        val saved = repository.save(entity) // Llama al bibliotecario (Repository)
        return saved.toResponse() // Traduce de vuelta para responder
    }
    ```

### Estación 4: El Mapper (El Traductor)
Convierte cajas de viaje (DTO) en archivos oficiales (Entity).
*   **Ejemplo en `StudentMapper.kt`**:
    ```kotlin
    fun StudentRequest.toEntity() = Student(
        name = this.name, // Copia el nombre de la caja al archivo
        email = this.email
    )
    ```

### Estación 5: La Entity (El Archivo Oficial)
Es lo que se guarda para siempre en la base de datos. Usa `@Entity`.
*   **Ejemplo en `Student.kt`**:
    ```kotlin
    @Entity
    @Table(name = "students") // Nombre de la tabla en la base de datos
    data class Student(
        @Id @GeneratedValue val id: Long = 0, // El sistema le da un número único
        var name: String = "",
        var email: String? = null
    )
    ```

### Estación 6: El Repository (El Bibliotecario)
Es quien guarda y busca en las estanterías (Base de Datos).
*   **Ejemplo en `StudentsRepository.kt`**:
    ```kotlin
    interface StudentsRepository : JpaRepository<Student, Long>
    // No necesita código, Spring Boot hace el trabajo sucio por nosotros.
    ```

---

## 🛠️ Fase 4: Herramientas y Errores

### 4.1 El Logger (El diario)
Es para el programador. Escribe mensajes en la consola para saber qué pasa.
*   `logger.info("Texto")` -> Escribe en pantalla: `INFO: Texto`.

### 4.2 El Símbolo `$` (Interpolación)
Sirve para meter variables dentro de un texto.
*   Si `val nombre = "Pepe"`, entonces `"Hola $nombre"` se convierte en `"Hola Pepe"`.

### 4.3 Excepciones y GlobalExceptionHandler
*   **Exception**: Es un grito de error. Ejemplo: `StudentNotFoundException`. Detiene el programa para no hacer daño.
*   **GlobalExceptionHandler**: Es el "Paramédico". Atrapa el grito de error y le responde al usuario con calma: "Lo siento, no encontré ese alumno", en lugar de que la aplicación se cierre.

---

## 🔄 Resumen del Flujo de Datos

1.  **Request** (Caja de envío) llega al **Controller** (Recepcionista).
2.  **Controller** anota en el **Logger** (Diario) y llama al **Service** (Cerebro).
3.  **Service** pide al **Mapper** (Traductor) convertir la caja en **Entity** (Ficha oficial).
4.  **Service** le da la **Entity** al **Repository** (Bibliotecario).
5.  **Repository** la guarda en la **Base de Datos**.
6.  **Service** convierte la ficha guardada en **Response** y el **Controller** la entrega.

¡Ahora tienes el mapa completo de cómo funciona tu sistema! 🚀
