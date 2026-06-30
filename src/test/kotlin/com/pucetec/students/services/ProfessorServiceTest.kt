package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.repositories.ProfessorRepository
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
 * Clase de pruebas unitarias para ProfessorService.
 * Se utiliza MockitoExtension para mockear las dependencias y lograr una cobertura del 100%.
 * Todos los comentarios están detallados en español.
 */
@ExtendWith(MockitoExtension::class)
class ProfessorServiceTest {

    // Mock del repositorio de profesores
    @Mock
    private lateinit var professorRepository: ProfessorRepository

    // Inyección de mocks en el servicio de profesores
    @InjectMocks
    private lateinit var professorService: ProfessorService

    // ==========================================
    // Pruebas para createProfessor
    // ==========================================

    /**
     * Prueba que createProfessor lance BlankNameException cuando el nombre del profesor está vacío.
     * Esto cubre la rama de validación del nombre.
     */
    @Test
    fun `createProfessor debe lanzar BlankNameException cuando el nombre esta vacio`() {
        // Arrange
        val request = ProfessorRequest(name = "", email = "profesor@puce.edu.ec")

        // Act & Assert
        assertThrows<BlankNameException> {
            professorService.createProfessor(request)
        }
    }

    /**
     * Prueba que createProfessor debe guardar y retornar el profesor de forma exitosa cuando los datos son válidos.
     * Esto cubre el camino feliz de creación de profesor.
     */
    @Test
    fun `createProfessor debe crear y retornar el profesor cuando los datos son validos`() {
        // Arrange
        val request = ProfessorRequest(name = "Juan Perez", email = "juan@puce.edu.ec")
        val savedProfessor = Professor(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")

        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(savedProfessor)

        // Act
        val response = professorService.createProfessor(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Juan Perez", response.name)
        assertEquals("juan@puce.edu.ec", response.email)
        verify(professorRepository, times(1)).save(any(Professor::class.java))
    }

    // ==========================================
    // Pruebas para getAllProfessors
    // ==========================================

    /**
     * Prueba que getAllProfessors devuelva la lista de profesores mapeada a DTOs de respuesta.
     */
    @Test
    fun `getAllProfessors debe retornar la lista de todos los profesores`() {
        // Arrange
        val professors = listOf(
            Professor(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec"),
            Professor(id = 2L, name = "Maria Gomez", email = "maria@puce.edu.ec")
        )

        `when`(professorRepository.findAll()).thenReturn(professors)

        // Act
        val response = professorService.getAllProfessors()

        // Assert
        assertEquals(2, response.size)
        assertEquals("Juan Perez", response[0].name)
        assertEquals("Maria Gomez", response[1].name)
        verify(professorRepository, times(1)).findAll()
    }

    // ==========================================
    // Pruebas para getProfessorById
    // ==========================================

    /**
     * Prueba que getProfessorById lance ProfessorNotFound cuando el profesor no existe en la base de datos.
     * Esto cubre la rama de error de búsqueda.
     */
    @Test
    fun `getProfessorById debe lanzar ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        `when`(professorRepository.findById(1L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.getProfessorById(1L)
        }
        verify(professorRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que getProfessorById retorne el profesor DTO de forma correcta si el profesor existe.
     * Esto cubre el camino feliz de búsqueda de profesor.
     */
    @Test
    fun `getProfessorById debe retornar el profesor cuando existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        // Act
        val response = professorService.getProfessorById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Juan Perez", response.name)
        verify(professorRepository, times(1)).findById(1L)
    }

    // ==========================================
    // Pruebas para updateProfessor
    // ==========================================

    /**
     * Prueba que updateProfessor lance ProfessorNotFound cuando se intenta actualizar un profesor inexistente.
     * Esto cubre la primera rama de error en actualización.
     */
    @Test
    fun `updateProfessor debe lanzar ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = ProfessorRequest(name = "Juan Perez", email = "juan@puce.edu.ec")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.updateProfessor(1L, request)
        }
        verify(professorRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateProfessor lance BlankNameException cuando el nuevo nombre propuesto está vacío.
     * Esto cubre la segunda rama de error en actualización.
     */
    @Test
    fun `updateProfessor debe lanzar BlankNameException cuando el nuevo nombre esta vacio`() {
        // Arrange
        val request = ProfessorRequest(name = "", email = "juan@puce.edu.ec")
        val existingProfessor = Professor(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))

        // Act & Assert
        assertThrows<BlankNameException> {
            professorService.updateProfessor(1L, request)
        }
        verify(professorRepository, times(1)).findById(1L)
    }

    /**
     * Prueba que updateProfessor actualice el nombre y correo del profesor y retorne la respuesta si todos los datos son válidos.
     * Esto cubre el camino feliz de actualización.
     */
    @Test
    fun `updateProfessor debe actualizar y retornar el profesor cuando los datos son validos`() {
        // Arrange
        val request = ProfessorRequest(name = "Juan Modificado", email = "juan_new@puce.edu.ec")
        val existingProfessor = Professor(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val savedProfessor = Professor(id = 1L, name = "Juan Modificado", email = "juan_new@puce.edu.ec")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(savedProfessor)

        // Act
        val response = professorService.updateProfessor(1L, request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Juan Modificado", response.name)
        assertEquals("juan_new@puce.edu.ec", response.email)
        verify(professorRepository, times(1)).findById(1L)
        verify(professorRepository, times(1)).save(any(Professor::class.java))
    }

    // ==========================================
    // Pruebas para deleteProfessor
    // ==========================================

    /**
     * Prueba que deleteProfessor lance ProfessorNotFound cuando el ID a eliminar no corresponde a ningún profesor.
     * Esto cubre la rama de error de eliminación.
     */
    @Test
    fun `deleteProfessor debe lanzar ProfessorNotFound cuando el profesor a eliminar no existe`() {
        // Arrange
        `when`(professorRepository.existsById(1L)).thenReturn(false)

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.deleteProfessor(1L)
        }
        verify(professorRepository, times(1)).existsById(1L)
    }

    /**
     * Prueba que deleteProfessor elimine correctamente el profesor cuando este sí existe.
     * Esto cubre el camino feliz de eliminación.
     */
    @Test
    fun `deleteProfessor debe eliminar el profesor correctamente cuando existe`() {
        // Arrange
        `when`(professorRepository.existsById(1L)).thenReturn(true)

        // Act
        professorService.deleteProfessor(1L)

        // Assert
        verify(professorRepository, times(1)).existsById(1L)
        verify(professorRepository, times(1)).deleteById(1L)
    }
}
