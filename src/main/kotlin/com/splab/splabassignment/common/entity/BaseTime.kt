package com.splab.splabassignment.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTime protected constructor(
    @CreatedDate
    @Column(name = "SYS_REG_DATE", columnDefinition = "TIMESTAMP")
    private var sysRegDate: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "SYS_MOD_DATE", columnDefinition = "TIMESTAMP")
    private var sysModDate: LocalDateTime? = null
)