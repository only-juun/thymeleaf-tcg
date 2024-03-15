package com.splab.splabassignment.card.service

import com.splab.splabassignment.card.repository.CardQueryRepository
import com.splab.splabassignment.card.service.usecase.LoadCardUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoadCardService(
    private val cardQueryRepository: CardQueryRepository,
) : LoadCardUseCase {
    override fun loadCardListByMemberId(memberId: Long) =
        cardQueryRepository.loadCardListByMemberId(memberId)
}