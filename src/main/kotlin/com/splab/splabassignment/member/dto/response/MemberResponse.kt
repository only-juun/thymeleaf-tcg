package com.splab.splabassignment.member.dto.response

import com.splab.splabassignment.level.entity.Level
import java.time.LocalDate

data class MemberResponse(
    val memberId: Long,
    val name: String,
    val email: String,
    val joinDate: LocalDate,
    val level: Level,
    val cardCount: Int
)