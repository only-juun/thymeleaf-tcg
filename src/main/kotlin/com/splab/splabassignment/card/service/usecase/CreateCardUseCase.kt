package com.splab.splabassignment.card.service.usecase

import com.splab.splabassignment.card.dto.request.CreateCardCommand

interface CreateCardUseCase {
    fun createCard(cardCommand: CreateCardCommand)
    fun isSerialNumberAvailable(serialNumber: Long?, game: String?): Boolean
}