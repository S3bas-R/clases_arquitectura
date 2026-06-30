package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.ProfessorRepository
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
import java.util.Optional
import kotlin.test.assertEquals

/**
 * Clase de pruebas unitarias para SubjectService.
 * Mockea los repositorios de Subject y Professor para cubrir todas las líneas y ramas (100% cobertura).
 * Todos los comentarios están detallados en español.
 */
@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    // Mock del repositorio de materias
    @Mock
    private lateinit var subjectRepository: SubjectRepository

    // Mock del repositorio de profesores
    @Mock
    private lateinit var professorRepository: ProfessorRepository

    // Inyección de mocks en el servicio de materias
    @InjectMocks
    private lateinit var subjectService: SubjectService

    // ==========================================
    // Pruebas para createSubject
    // ==========================================

    /**
     * Prueba que createSubject lance BlankNameException cuando el nombre de la materia está vacío.
     */
    @Test
    fun `createSubject debe lanzar BlankNameException cuando el nombre esta vacio`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "AE101", description = "Clase de Arquitectura", professorId = 1L)

        // Act & Assert
        assertThrows<BlankNameException> {
            subjectService.createSubject(request)
        }
    }

    /**
     * Prueba que createSubject lance BlankNameException cuando el código de la materia está vacío pero el nombre es válido.
     */
    @Test
    fun `createSubject debe lanzar BlankNameException cuando el codigo esta vacio`() {
        // Arrange
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "", description = "Clase de Arquitectura", professorId = 1L)

        // Act & Assert
        assertThrows<BlankNameException> {
            subjectService.createSubject(request)
        }
    }

    /**
     * Prueba que createSubject lance ProfessorNotFound cuando el profesor asignado no existe.
     */
    @Test
    fun `createSubject debe lanzar ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "AE101", description = "Clase de Arquitectura", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.createSubject(request)
        }
        verify(professorRepository, times(1)).findById(99L)
    }

    /**
     * Prueba que createSubject cree exitosamente la materia y retorne el DTO de respuesta.
     */
    @Test
    fun `createSubject debe crear y retornar la materia cuando los datos son validos`() {
        // Arrange
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "AE101", description = "Clase de Arquitectura", professorId = 1L)
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val savedSubject = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE101", description = "Clase de Arquitectura", professor = professor)

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(savedSubject)

        // Act
        val response = subjectService.createSubject(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Arquitectura Empresarial", response.name)
        assertEquals("AE101", response.code)
        assertEquals("Dr. Gomez", response.professor?.name)
        verify(professorRepository, times(1)).findById(1L)
        verify(subjectRepository, times(1)).save(any(Subject::class.java))
    }

    // ==========================================
    // Pruebas para getAllSubjects
    // ==========================================

    /**
     * Prueba que getAllSubjects devuelva la lista de materias registradas en el sistema.
     */
    @Test
    fun `getAllSubjects debe retornar la lista de todas las materias`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subjects = listOf(
            Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A", professor = professor),
            Subject(id = 2L, name = "Materia B", code = "B2", description = "Desc B", professor = professor)
        )

        `when`(subjectRepository.findAll()).thenReturn(subjects)

        // Act
        val response = subjectService.getAllSubjects()

        // Assert
        assertEquals(2, response.size)
        assertEquals("Materia A", response[0].name)
        assertEquals("Materia B", response[1].name)
        verify(subjectRepository, times(1)).findAll()
    }

    // ==========================================
    // Pruebas para getSubjectById
    // ==========================================

    /**
     * Prueba que getSubjectById lance SubjectNotFound cuando la materia buscada no existe.
     */
    @Test
    fun `getSubjectById debe lanzar SubjectNotFound cuando la materia no existe`() {
        // Arrange
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.getSubjectById(1L)
        }
        verify(subjectRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que getSubjectById devuelva la materia DTO cuando sí existe en la base de datos.
     */
    @Test
    fun `getSubjectById debe retornar la materia cuando existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val subject = Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        // Act
        val response = subjectService.getSubjectById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Materia A", response.name)
        verify(subjectRepository, times(1)).findById(1L)
    }

    // ==========================================
    // Pruebas para updateSubject
    // ==========================================

    /**
     * Prueba que updateSubject lance SubjectNotFound cuando la materia a actualizar no existe en el sistema.
     */
    @Test
    fun `updateSubject debe lanzar SubjectNotFound cuando la materia a actualizar no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Materia A Modificada", code = "A1", description = "Desc A", professorId = 1L)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateSubject lance BlankNameException cuando el nuevo nombre está en blanco (la materia existe).
     */
    @Test
    fun `updateSubject debe lanzar BlankNameException cuando el nuevo nombre esta vacio`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "A1", description = "Desc A", professorId = 1L)
        val existingSubject = Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A")
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))

        // Act & Assert
        assertThrows<BlankNameException> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateSubject lance BlankNameException cuando el nuevo código está en blanco (materia existe, nombre válido).
     */
    @Test
    fun `updateSubject debe lanzar BlankNameException cuando el nuevo codigo esta vacio`() {
        // Arrange
        val request = SubjectRequest(name = "Materia A Modificada", code = "", description = "Desc A", professorId = 1L)
        val existingSubject = Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A")
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))

        // Act & Assert
        assertThrows<BlankNameException> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateSubject lance ProfessorNotFound cuando el nuevo profesor especificado no existe en el sistema.
     */
    @Test
    fun `updateSubject debe lanzar ProfessorNotFound cuando el nuevo profesor no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Materia A Modificada", code = "A1-Mod", description = "Desc A", professorId = 99L)
        val existingSubject = Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A")

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, times(1)).findById(1L)
        verify(professorRepository, times(1)).findById(99L)
    }

    /**
     * Prueba que updateSubject actualice los campos y retorne la materia cuando todos los parámetros son correctos.
     */
    @Test
    fun `updateSubject debe actualizar y retornar la materia cuando los datos son validos`() {
        // Arrange
        val request = SubjectRequest(name = "Materia A Modificada", code = "A1-Mod", description = "Desc A Mod", professorId = 1L)
        val existingSubject = Subject(id = 1L, name = "Materia A", code = "A1", description = "Desc A")
        val professor = Professor(id = 1L, name = "Dr. Gomez", email = "gomez@puce.edu.ec")
        val savedSubject = Subject(id = 1L, name = "Materia A Modificada", code = "A1-Mod", description = "Desc A Mod", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(savedSubject)

        // Act
        val response = subjectService.updateSubject(1L, request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Materia A Modificada", response.name)
        assertEquals("A1-Mod", response.code)
        assertEquals("Dr. Gomez", response.professor?.name)
        verify(subjectRepository, times(1)).findById(1L)
        verify(professorRepository, times(1)).findById(1L)
        verify(subjectRepository, times(1)).save(any(Subject::class.java))
    }

    // ==========================================
    // Pruebas para deleteSubject
    // ==========================================

    /**
     * Prueba que deleteSubject lance SubjectNotFound cuando el ID a eliminar no corresponde a ninguna materia.
     */
    @Test
    fun `deleteSubject debe lanzar SubjectNotFound cuando la materia no existe`() {
        // Arrange
        `when`(subjectRepository.existsById(1L)).thenReturn(false)

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.deleteSubject(1L)
        }
        verify(subjectRepository, times(1)).existsById(1L)
    }

    /**
     * Prueba que deleteSubject elimine la materia del repositorio cuando esta existe.
     */
    @Test
    fun `deleteSubject debe eliminar la materia cuando existe`() {
        // Arrange
        `when`(subjectRepository.existsById(1L)).thenReturn(true)

        // Act
        subjectService.deleteSubject(1L)

        // Assert
        verify(subjectRepository, times(1)).existsById(1L)
        verify(subjectRepository, times(1)).deleteById(1L)
    }
}
