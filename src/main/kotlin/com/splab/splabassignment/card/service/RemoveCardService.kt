package com.splab.splabassignment.card.service

import com.splab.splabassignment.card.repository.CardRepository
import com.splab.splabassignment.card.service.usecase.RemoveCardUseCase
import com.splab.splabassignment.level.service.usecase.UpdateLevelUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class RemoveCardService(
    private val cardRepository: CardRepository,
    private val updateLevelUseCase: UpdateLevelUseCase
) : RemoveCardUseCase {
    override fun removeCard(cardId: Long, memberId: Long) {
        val card = cardRepository.findByIdAndNotDeleted(cardId)
            ?: throw NoSuchElementException("카드를 찾을 수 없습니다. cardId: $cardId")

        val deleteAt = LocalDateTime.now()
        card.deleteCard(deleteAt)
        updateLevelUseCase.updateMemberCardInfo(memberId)
    }
}