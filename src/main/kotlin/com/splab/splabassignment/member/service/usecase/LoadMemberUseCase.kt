package com.splab.splabassignment.member.service.usecase

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.dto.response.MemberResponse
import com.splab.splabassignment.member.entity.Member

interface LoadMemberUseCase {

    fun loadMemberListByCondition(name: String?, level: Level?) : List<MemberResponse>

    fun loadMemberById(memberId: Long) : Member?
}