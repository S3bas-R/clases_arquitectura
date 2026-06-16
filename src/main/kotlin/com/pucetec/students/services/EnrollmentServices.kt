package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student
import com.pucetec.students.repositories.StudentsRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentServices (
    private val studentsRepository: StudentsRepository,
    private val subjectRepository: SubjectRepository,
    private val enrollmentRepository: EnrollmentRepository
) {
    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        val student:
    }
}