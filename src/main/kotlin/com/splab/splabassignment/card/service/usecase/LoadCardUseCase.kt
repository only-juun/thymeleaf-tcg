package com.splab.splabassignment.card.service.usecase

import com.splab.splabassignment.card.dto.response.CardResponse

interface LoadCardUseCase {
    fun loadCardListByMemberId(memberId: Long) : List<CardResponse>
}