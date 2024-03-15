package com.splab.splabassignment.member.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD, VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DatePatternValidator::class])
annotation class DatePattern(
    val pattern: String,
    val message: String = "날짜 형식에 맞지 않습니다. yyyy-MM-dd 형식이어야 합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)