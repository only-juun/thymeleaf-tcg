package com.splab.splabassignment.member.service.usecase

import com.splab.splabassignment.member.dto.request.CreateMemberCommand

interface CreateMemberUseCase {
    fun createMember(memberFormData: CreateMemberCommand)
}