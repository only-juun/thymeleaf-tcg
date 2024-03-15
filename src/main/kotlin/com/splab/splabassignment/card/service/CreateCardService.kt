package com.splab.splabassignment.card.service

import com.splab.splabassignment.card.dto.request.CreateCardCommand
import com.splab.splabassignment.card.repository.CardRepository
import com.splab.splabassignment.card.service.usecase.CreateCardUseCase
import com.splab.splabassignment.level.service.usecase.UpdateLevelUseCase
import com.splab.splabassignment.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateCardService(
    private val cardRepository: CardRepository,
    private val memberRepository: MemberRepository,
    private val updateLevelUseCase: UpdateLevelUseCase
) : CreateCardUseCase {

    override fun createCard(cardCommand: CreateCardCommand) {
        val member = memberRepository.findByIdAndNotDeleted(cardCommand.memberId!!)
            ?: throw IllegalArgumentException("회원를 찾을 수 없습니다. memberId: ${cardCommand.memberId}")

        val card = cardCommand.mapToEntity(member)

        cardRepository.save(card)
        updateLevelUseCase.updateMemberCardInfo(cardCommand.memberId)
    }

    override fun isSerialNumberAvailable(serialNumber: Long?, game: String?): Boolean {
        if (serialNumber == null || game == null) {
            // 일련번호나 게임 이름이 제공되지 않은 경우, 검증할 수 없으므로 true 반환
            return true
        }

        val card = cardRepository.findBySerialNumberAndGame(serialNumber, game)
        // 조회된 카드의 게임이 입력받은 게임과 다른 경우에만 true 반환
        return card?.game != game
    }
}