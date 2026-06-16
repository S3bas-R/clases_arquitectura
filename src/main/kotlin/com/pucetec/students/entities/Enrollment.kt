package com.pucetec.students.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.query.common.FetchClauseType
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Entity
@Table(name = "enrollments")
class Enrollment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val status: String = "",

    @Column (name= "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    val subject: Subject,

    @ManyToOne(fetch = FetchType.LAZY)
    val


    )