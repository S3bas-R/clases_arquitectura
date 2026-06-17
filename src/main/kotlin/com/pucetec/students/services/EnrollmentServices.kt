package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentsRepository
import com.pucetec.students.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class EnrollmentServices(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentsRepository: StudentsRepository,
    private val subjectRepository: SubjectRepository
) {
    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        val student = studentsRepository.findById(request.studentId).orElseThrow()
        val subject = subjectRepository.findById(request.subjectId).orElseThrow()
        val enrollment = Enrollment(
            student = student,
            subject = subject
        )
        return enrollmentRepository.save(enrollment).toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        return enrollmentRepository.findAll().map { it.toResponse() }
    }
}
