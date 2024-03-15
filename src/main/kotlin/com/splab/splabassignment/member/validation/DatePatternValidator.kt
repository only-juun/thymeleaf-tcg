package com.splab.splabassignment.member.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DatePatternValidator : ConstraintValidator<DatePattern, String?> {
    private lateinit var pattern: String

    override fun initialize(constraintAnnotation: DatePattern) {
        pattern = constraintAnnotation.pattern
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true // @NotNull로 처리

        return try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern))
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
}