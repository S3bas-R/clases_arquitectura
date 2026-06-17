package com.pucetec.students.repositories

// Importamos la ficha oficial (Student) y las herramientas de Spring.
import com.pucetec.students.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// @Repository: Sticker que dice que esta clase es el "Bibliotecario".
@Repository
// interface: Es un contrato. No escribimos código aquí.
// JpaRepository<Student, Long>: Al heredar de aquí, este bibliotecario aprende automáticamente
// a Buscar, Guardar, Borrar y Actualizar 'Students' usando su ID (que es de tipo Long).
interface StudentsRepository : JpaRepository<Student, Long>
