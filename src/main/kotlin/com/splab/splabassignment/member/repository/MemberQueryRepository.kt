package com.splab.splabassignment.member.repository

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.dto.response.MemberResponse

interface MemberQueryRepository {

    fun loadMemberListByCondition(name: String?, level: Level?) : List<MemberResponse>
}