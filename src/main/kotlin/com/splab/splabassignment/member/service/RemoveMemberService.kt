package com.splab.splabassignment.member.service

import com.splab.splabassignment.member.repository.MemberRepository
import com.splab.splabassignment.member.service.usecase.RemoveMemberUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class RemoveMemberService(
    private val memberRepository: MemberRepository,
) : RemoveMemberUseCase {
    override fun removeMember(memberId: Long) {
        val member = memberRepository.findById(memberId).orElseThrow {
            IllegalArgumentException("멤버를 찾을 수 없습니다. memberId: $memberId")
        }

        val deleteAt = LocalDateTime.now()
        member.cards
            .filter { it.deletedAt == null }
            .forEach { card -> card.deleteCard(deleteAt) }

        member.deleteMember(deleteAt)

    }
}