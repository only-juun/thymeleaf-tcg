package com.splab.splabassignment.member.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class DateRangeValidator : ConstraintValidator<DateRange, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true

        try {
            val localDateValue = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val oneYearAgo = today.minus(1, ChronoUnit.YEARS)

            return !localDateValue.isAfter(today) && !localDateValue.isBefore(oneYearAgo)
        } catch (e: DateTimeParseException) {
            // 파싱 실패는 유효하지 않은 형식을 의미함
            return true
        }
    }
}