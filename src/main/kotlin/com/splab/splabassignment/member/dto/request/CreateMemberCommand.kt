package com.splab.splabassignment.member.dto.request

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.entity.Member
import com.splab.splabassignment.member.validation.DatePattern
import com.splab.splabassignment.member.validation.DateRange
import com.splab.splabassignment.member.validation.NotDuplicatedEmail
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

data class CreateMemberCommand(

    @field:NotBlank(message = "이름은 비어있을 수 없습니다.")
    @field:Length(min = 2, max = 100, message = "이름은 2자 이상 100자의 길이여야 합니다.")
    @field:Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "한글 또는 영어만 입력할 수 있습니다.")
    val memberName: String?,

    @field:NotDuplicatedEmail
    @field:Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\$",
        message = "이메일 형식에 맞지 않습니다.")
    @field:NotBlank(message = "이메일은 비어있을 수 없습니다.")
    val memberEmail: String?,

    @field:DateRange
    @field:NotBlank(message = "날짜는 비어있을 수 없습니다.")
    @field:DatePattern(pattern = "yyyy-MM-dd", message = "날짜 형식에 맞지 않습니다. yyyy-MM-dd 형식이어야 합니다.")
    val joinDate: String?
) {

    fun mapToEntity() = Member(
        memberName = memberName!!,
        memberEmail = memberEmail!!,
        joinDate = LocalDate.parse(joinDate),
        level = Level.BRONZE,
        cardCount = 0,
        cardTotalPrice = 0.0
    )
}