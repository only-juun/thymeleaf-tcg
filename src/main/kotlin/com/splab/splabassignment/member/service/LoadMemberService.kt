package com.splab.splabassignment.member.service

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.entity.Member
import com.splab.splabassignment.member.repository.MemberQueryRepository
import com.splab.splabassignment.member.repository.MemberRepository
import com.splab.splabassignment.member.service.usecase.LoadMemberUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoadMemberService(
    private val memberRepository: MemberRepository,
    private val memberQueryRepository: MemberQueryRepository,
) : LoadMemberUseCase {
    override fun loadMemberListByCondition(name: String?, level: Level?) =
        memberQueryRepository.loadMemberListByCondition(name, level)

    override fun loadMemberById(memberId: Long): Member? =
        memberRepository.findByIdAndNotDeleted(memberId)
}