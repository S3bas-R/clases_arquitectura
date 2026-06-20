package com.pucetec.students.entities

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "subjects")
class Subject (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    var name: String = "",
    var code: String? = "",
    var description: String? = "",

    @ManyToOne(fetch = FetchType.LAZY)
    var professor: Professor? = null,

)
