package com.pucetec.students.dto


data class SubjectRequest(
   val name: String,
   val code: String,
   val description: String,
)

data class SubjectResponse(
    val id: Long,
    val name: String,
    val code: String,
    val professor: ProfessorResponse,
)