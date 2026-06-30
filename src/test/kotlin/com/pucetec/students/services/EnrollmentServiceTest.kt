package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentsRepository
import com.pucetec.students.repositories.SubjectRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertEquals

/**
 * Clase de pruebas unitarias para EnrollmentService.
 * Mockea los repositorios EnrollmentRepository, StudentsRepository y SubjectRepository
 * para garantizar la cobertura del 100% de líneas y ramas de decisión.
 * Todos los comentarios están detallados en español.
 */
@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    // Mock del repositorio de inscripciones
    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    // Mock del repositorio de estudiantes (utilizado en EnrollmentService)
    @Mock
    private lateinit var studentsRepository: StudentsRepository

    // Mock del repositorio de materias
    @Mock
    private lateinit var subjectRepository: SubjectRepository

    // Inyección de mocks en el servicio de inscripciones
    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    // ==========================================
    // Pruebas para createEnrollment
    // ==========================================

    /**
     * Prueba que createEnrollment lance StudentNotFoundException cuando el estudiante no existe.
     * Esto cubre la primera rama de error al validar el estudiante.
     */
    @Test
    fun `createEnrollment debe lanzar StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        `when`(studentsRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
        verify(studentsRepository, times(1)).findById(99L)
    }

    /**
     * Prueba que createEnrollment lance SubjectNotFound cuando el estudiante sí existe pero la materia no.
     * Esto cubre la segunda rama de error al validar la materia.
     */
    @Test
    fun `createEnrollment debe lanzar SubjectNotFound cuando la materia no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        val student = Student(id = 1L, name = "Juan Lopez", email = "juan@puce.edu.ec")

        `when`(studentsRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            enrollmentService.createEnrollment(request)
        }
        verify(studentsRepository, times(1)).findById(1L)
        verify(subjectRepository, times(1)).findById(99L)
    }

    /**
     * Prueba que createEnrollment guarde y retorne la inscripción de forma exitosa cuando estudiante y materia existen.
     * Esto cubre el camino feliz de creación de inscripción.
     */
    @Test
    fun `createEnrollment debe crear y retornar la inscripcion cuando los datos son validos`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 2L)
        val student = Student(id = 1L, name = "Juan Lopez", email = "juan@puce.edu.ec")
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subject = Subject(id = 2L, name = "Arquitectura", code = "AE101", description = "Clase", professor = professor)
        val savedEnrollment = Enrollment(
            id = 10L,
            status = "INSCRITO",
            createdAt = LocalDateTime.now(),
            student = student,
            subject = subject
        )

        `when`(studentsRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(2L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(savedEnrollment)

        // Act
        val response = enrollmentService.createEnrollment(request)

        // Assert
        assertEquals(10L, response.id)
        assertEquals("INSCRITO", response.status)
        assertEquals("Juan Lopez", response.student.name)
        assertEquals("Arquitectura", response.subject.name)
        verify(studentsRepository, times(1)).findById(1L)
        verify(subjectRepository, times(1)).findById(2L)
        verify(enrollmentRepository, times(1)).save(any(Enrollment::class.java))
    }

    // ==========================================
    // Pruebas para getAllEnrollments
    // ==========================================

    /**
     * Prueba que getAllEnrollments devuelva la lista de inscripciones mapeadas.
     * Esto cubre el camino feliz de obtener todas las inscripciones.
     */
    @Test
    fun `getAllEnrollments debe retornar la lista de todas las inscripciones`() {
        // Arrange
        val student = Student(id = 1L, name = "Juan Lopez", email = "juan@puce.edu.ec")
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subject = Subject(id = 2L, name = "Arquitectura", code = "AE101", description = "Clase", professor = professor)
        val enrollments = listOf(
            Enrollment(id = 10L, status = "INSCRITO", createdAt = LocalDateTime.now(), student = student, subject = subject),
            Enrollment(id = 11L, status = "COMPLETADO", createdAt = LocalDateTime.now(), student = student, subject = subject)
        )

        `when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        // Act
        val response = enrollmentService.getAllEnrollments()

        // Assert
        assertEquals(2, response.size)
        assertEquals(10L, response[0].id)
        assertEquals("INSCRITO", response[0].status)
        assertEquals(11L, response[1].id)
        assertEquals("COMPLETADO", response[1].status)
        verify(enrollmentRepository, times(1)).findAll()
    }

    // ==========================================
    // Pruebas para getEnrollmentById
    // ==========================================

    /**
     * Prueba que getEnrollmentById lance EnrollmentNotFound cuando la inscripción no existe.
     * Esto cubre la rama de error de búsqueda.
     */
    @Test
    fun `getEnrollmentById debe lanzar EnrollmentNotFound cuando la inscripcion no existe`() {
        // Arrange
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.getEnrollmentById(10L)
        }
        verify(enrollmentRepository, times(1)).findById(10L)
    }

    /**
     * Prueba que getEnrollmentById devuelva la inscripción cuando sí existe.
     * Esto cubre el camino feliz de búsqueda de inscripción por ID.
     */
    @Test
    fun `getEnrollmentById debe retornar la inscripcion cuando existe`() {
        // Arrange
        val student = Student(id = 1L, name = "Juan Lopez", email = "juan@puce.edu.ec")
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subject = Subject(id = 2L, name = "Arquitectura", code = "AE101", description = "Clase", professor = professor)
        val enrollment = Enrollment(id = 10L, status = "INSCRITO", createdAt = LocalDateTime.now(), student = student, subject = subject)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment))

        // Act
        val response = enrollmentService.getEnrollmentById(10L)

        // Assert
        assertEquals(10L, response.id)
        assertEquals("INSCRITO", response.status)
        verify(enrollmentRepository, times(1)).findById(10L)
    }

    // ==========================================
    // Pruebas para updateEnrollment
    // ==========================================

    /**
     * Prueba que updateEnrollment lance EnrollmentNotFound si se intenta actualizar el estado de una inscripción inexistente.
     * Esto cubre la rama de error de actualización.
     */
    @Test
    fun `updateEnrollment debe lanzar EnrollmentNotFound cuando la inscripcion no existe`() {
        // Arrange
        val request = EnrollmentUpdateRequest(status = "REPROBADO")
        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.updateEnrollment(10L, request)
        }
        verify(enrollmentRepository, times(1)).findById(10L)
    }

    /**
     * Prueba que updateEnrollment actualice el estado y devuelva el DTO modificado si la inscripción existe.
     * Esto cubre el camino feliz de actualizar el estado de una inscripción.
     */
    @Test
    fun `updateEnrollment debe actualizar el estado y retornar la inscripcion cuando existe`() {
        // Arrange
        val request = EnrollmentUpdateRequest(status = "REPROBADO")
        val student = Student(id = 1L, name = "Juan Lopez", email = "juan@puce.edu.ec")
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subject = Subject(id = 2L, name = "Arquitectura", code = "AE101", description = "Clase", professor = professor)
        val existingEnrollment = Enrollment(id = 10L, status = "INSCRITO", createdAt = LocalDateTime.now(), student = student, subject = subject)
        val savedEnrollment = Enrollment(id = 10L, status = "REPROBADO", createdAt = LocalDateTime.now(), student = student, subject = subject)

        `when`(enrollmentRepository.findById(10L)).thenReturn(Optional.of(existingEnrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(savedEnrollment)

        // Act
        val response = enrollmentService.updateEnrollment(10L, request)

        // Assert
        assertEquals(10L, response.id)
        assertEquals("REPROBADO", response.status)
        verify(enrollmentRepository, times(1)).findById(10L)
        verify(enrollmentRepository, times(1)).save(any(Enrollment::class.java))
    }

    // ==========================================
    // Pruebas para deleteEnrollment
    // ==========================================

    /**
     * Prueba que deleteEnrollment lance EnrollmentNotFound si la inscripción a eliminar no existe.
     * Esto cubre la rama de error de eliminación.
     */
    @Test
    fun `deleteEnrollment debe lanzar EnrollmentNotFound cuando la inscripcion a eliminar no existe`() {
        // Arrange
        `when`(enrollmentRepository.existsById(10L)).thenReturn(false)

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.deleteEnrollment(10L)
        }
        verify(enrollmentRepository, times(1)).existsById(10L)
    }

    /**
     * Prueba que deleteEnrollment elimine la inscripción correctamente si esta existe.
     * Esto cubre el camino feliz de eliminación.
     */
    @Test
    fun `deleteEnrollment debe eliminar la inscripcion correctamente cuando existe`() {
        // Arrange
        `when`(enrollmentRepository.existsById(10L)).thenReturn(true)

        // Act
        enrollmentService.deleteEnrollment(10L)

        // Assert
        verify(enrollmentRepository, times(1)).existsById(10L)
        verify(enrollmentRepository, times(1)).deleteById(10L)
    }
}
