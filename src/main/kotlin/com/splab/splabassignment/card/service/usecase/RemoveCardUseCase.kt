package com.splab.splabassignment.card.service.usecase

interface RemoveCardUseCase {

    fun removeCard(cardId: Long, memberId: Long)
}