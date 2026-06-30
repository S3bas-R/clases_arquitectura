package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
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
import java.util.Optional
import kotlin.test.assertEquals

// @ExtendWith activa la integración de Mockito con JUnit
@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `createStudent should throw BlankNameException when name is blank`() {
        val request = StudentRequest(name = "", email = "test@test.com")

        assertThrows<BlankNameException> {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent should return valid StudentResponse when name is not blank`() {
        val request = StudentRequest(
            name = "test",
            email = "test@test.com"
        )

        val savedStudent = Student(
            id = 1L,
            name = "test",
            email = "test@test.com"
        )

        `when`(studentRepository.save(any(Student::class.java)))
            .thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertEquals("test@test.com", response.email)
    }

    @Test
    fun `createStudent should return valid StudentResponse with empty email when email is null`() {
        val request = StudentRequest(
            name = "test",
            email = null
        )

        val savedStudent = Student(
            id = 1L,
            name = request.name,
            email = request.email
        )

        `when`(studentRepository.save(any(Student::class.java)))
            .thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertEquals(null, response.email)
    }

    @Test
    fun `getAllStudents should return a list of StudentsResponse`() {
        val students = listOf(
            Student(
                id = 1L,
                name = "ana",
                email = "ana@puce.com"
            ),
            Student(
                id = 2L,
                name = "bob",
                email = "bob@puce.com"
            ),
            Student(
                id = 3L,
                name = "joaquin",
                email = "joaquin@puce.com"
            )
        )

        `when`(studentRepository.findAll()).thenReturn(students)

        val response = studentService.getAllStudents()

        assertEquals(3, response.size)
        assertEquals("ana", response[0].name)
    }

    @Test
    fun `getStudentById should return a StudentResponse`() {
        val student = Student(
            id = 1L,
            name = "test",
            email = "test@test.com"
        )

        `when`(studentRepository.findById(any(Long::class.java)))
            .thenReturn(Optional.of(student))

        val response = studentService.getStudentById(1L)
        assertEquals(1L, response.id)
    }

    @Test
    fun `getStudentById should throw StudentNotFoundException`() {
        `when`(studentRepository.findById(any(Long::class.java)))
            .thenReturn(Optional.empty())

        assertThrows<StudentNotFoundException> {
            studentService.getStudentById(1L)
        }
    }

    // ==========================================
    // Pruebas añadidas para updateStudent
    // ==========================================

    /**
     * Prueba que updateStudent lance StudentNotFoundException cuando el estudiante no existe.
     * Esto cubre la primera rama de error al intentar actualizar.
     */
    @Test
    fun `updateStudent debe lanzar StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu.ec")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.updateStudent(1L, request)
        }
        verify(studentRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateStudent lance BlankNameException cuando el nuevo nombre provisto está en blanco.
     * Esto cubre la segunda rama de error en actualización.
     */
    @Test
    fun `updateStudent debe lanzar BlankNameException cuando el nuevo nombre esta vacio`() {
        // Arrange
        val request = StudentRequest(name = "", email = "ana@puce.edu.ec")
        val existingStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu.ec")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))

        // Act & Assert
        assertThrows<BlankNameException> {
            studentService.updateStudent(1L, request)
        }
        verify(studentRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateStudent actualice los datos del estudiante de forma correcta si toda la información es válida.
     * Esto cubre el camino feliz de actualización de estudiante.
     */
    @Test
    fun `updateStudent debe actualizar y retornar el estudiante cuando los datos son validos`() {
        // Arrange
        val request = StudentRequest(name = "Ana Lopez Modificada", email = "ana_new@puce.edu.ec")
        val existingStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu.ec")
        val savedStudent = Student(id = 1L, name = "Ana Lopez Modificada", email = "ana_new@puce.edu.ec")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

        // Act
        val response = studentService.updateStudent(1L, request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Ana Lopez Modificada", response.name)
        assertEquals("ana_new@puce.edu.ec", response.email)
        verify(studentRepository, times(1)).findById(1L)
        verify(studentRepository, times(1)).save(any(Student::class.java))
    }

    // ==========================================
    // Pruebas añadidas para deleteStudent
    // ==========================================

    /**
     * Prueba que deleteStudent lance StudentNotFoundException cuando el ID a eliminar no corresponde a ningún estudiante.
     * Esto cubre la rama de error de eliminación.
     */
    @Test
    fun `deleteStudent debe lanzar StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        `when`(studentRepository.existsById(1L)).thenReturn(false)

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.deleteStudent(1L)
        }
        verify(studentRepository, times(1)).existsById(1L)
    }

    /**
     * Prueba que deleteStudent elimine al estudiante cuando este existe.
     * Esto cubre el camino feliz de eliminación.
     */
    @Test
    fun `deleteStudent debe eliminar al estudiante cuando existe`() {
        // Arrange
        `when`(studentRepository.existsById(1L)).thenReturn(true)

        // Act
        studentService.deleteStudent(1L)

        // Assert
        verify(studentRepository, times(1)).existsById(1L)
        verify(studentRepository, times(1)).deleteById(1L)
    }
}

