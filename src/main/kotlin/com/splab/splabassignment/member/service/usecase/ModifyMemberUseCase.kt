package com.splab.splabassignment.member.service.usecase

import com.splab.splabassignment.member.dto.request.ModifyMemberCommand

interface ModifyMemberUseCase {
    fun loadTargetMember(memberId: Long): ModifyMemberCommand
    fun modifyMember(memberFormData: ModifyMemberCommand)
    fun isEmailAvailable(memberId: Long, memberEmail: String): Boolean
}