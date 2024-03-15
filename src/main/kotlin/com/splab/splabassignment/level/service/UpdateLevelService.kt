package com.splab.splabassignment.level.service

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.level.event.ModifyMemberLevelNotify
import com.splab.splabassignment.level.service.usecase.UpdateLevelUseCase
import com.splab.splabassignment.member.repository.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateLevelService(
    private val memberRepository: MemberRepository,
    private val eventPublisher: ApplicationEventPublisher,
): UpdateLevelUseCase {
    override fun updateMemberCardInfo(memberId: Long) {
        val member = memberRepository.findByIdAndNotDeleted(memberId)
            ?: throw NoSuchElementException("멤버를 찾을 수 없습니다. memberId: $memberId")

        val cards = member.cards.filter { it.deletedAt == null }

        // 회원이 가진 카드의 개수를 계산합니다.
        val cardCount = cards.size

        // 유효 카드만 필터링합니다. (가격이 0 초과인 카드)
        val validCards = cards.filter { it.price > 0 }

        // 유효 카드의 총 가격을 계산합니다.
        val cardTotalPrice = validCards.sumOf { it.price }

        // 서로 다른 게임의 수를 계산합니다 (유효 카드를 기준으로).
        val distinctGamesCount = validCards.map { it.game }.distinct().size

        // 레벨 결정 로직
        val newLevel = when {
            distinctGamesCount >= 2 && (validCards.size >= 4 || cardTotalPrice >= 100) -> Level.GOLD
            validCards.isNotEmpty() -> Level.SILVER
            else -> Level.BRONZE
        }

        if (member.level != newLevel) {
            eventPublisher.publishEvent(
                ModifyMemberLevelNotify(memberName = member.memberName, oldLevel = member.level, newLevel= newLevel)
            )

            member.updateLevel(newLevel)
        }

        member.updateMemberCardInfo(cardCount, cardTotalPrice)
    }
}