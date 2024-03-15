package com.splab.splabassignment.member.service

import com.splab.splabassignment.level.event.CreateMemberLevelNotify
import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.dto.request.CreateMemberCommand
import com.splab.splabassignment.member.repository.MemberRepository
import com.splab.splabassignment.member.service.usecase.CreateMemberUseCase
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateMemberService(
    private val memberRepository: MemberRepository,
    private val eventPublisher: ApplicationEventPublisher,
) : CreateMemberUseCase {

    override fun createMember(memberFormData: CreateMemberCommand) {
        val member = memberFormData.mapToEntity()
        eventPublisher.publishEvent(
            CreateMemberLevelNotify(memberName = memberFormData.memberName!!, newLevel = Level.BRONZE)
        )
        memberRepository.save(member)
    }
}