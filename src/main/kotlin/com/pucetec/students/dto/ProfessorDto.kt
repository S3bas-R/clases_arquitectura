package com.pucetec.students.dto

import javax.management.StringValueExp

/***
 * {name: Sebastian, email: sebastian@puce.edu.ec},
 * {name: Sebastian}
 */

data class ProfessorRequest(
    val name: String,
    val email: String? = null, // Optional
    )

/***
 * {id: 1, name: Sebastian, email:sebstian@puce.edu.ec}
 * {id: 2, name: Sebastian}
 */


data class ProfessorResponse(
    val id: Long,
    val name: String,
    val email: String?,
)