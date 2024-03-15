package com.splab.splabassignment.member.service

import com.splab.splabassignment.member.dto.request.ModifyMemberCommand
import com.splab.splabassignment.member.repository.MemberRepository
import com.splab.splabassignment.member.service.usecase.ModifyMemberUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class ModifyMemberService(
    private val memberRepository: MemberRepository,
) : ModifyMemberUseCase {
    override fun loadTargetMember(memberId: Long): ModifyMemberCommand {
        val findMember = memberRepository.findByIdAndNotDeleted(memberId)
            ?: throw NoSuchElementException("멤버를 찾을 수 없습니다. memberId: $memberId")

        return ModifyMemberCommand(
            memberId = memberId,
            memberName = findMember.memberName,
            memberEmail = findMember.memberEmail,
            joinDate = findMember.joinDate.toString()
        )
    }

    override fun modifyMember(memberFormData: ModifyMemberCommand) {
        val memberToUpdate = memberRepository.findByIdAndNotDeleted(memberFormData.memberId)
            ?: throw NoSuchElementException("멤버를 찾을 수 없습니다. memberId: ${memberFormData.memberId}")

        memberToUpdate.modifyMember(
            name = memberFormData.memberName!!,
            email = memberFormData.memberEmail!!,
            joinDate = LocalDate.parse(memberFormData.joinDate!!)
        )
    }

    override fun isEmailAvailable(memberId: Long, memberEmail: String): Boolean {
        val isEmailInUseByAnotherMember = memberRepository.existsByMemberEmailAndMemberIdNot(
            memberEmail = memberEmail,
            memberIdNot = memberId
        )
        // 다른 회원이 이미 사용 중인 이메일이 없으면 true(고유), 있으면 false(비고유)
        return !isEmailInUseByAnotherMember
    }
}