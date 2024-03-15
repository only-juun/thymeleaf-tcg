package com.splab.splabassignment.card.dto.response

data class CardResponse(
    val cardId: Long,
    val game: String,
    val title: String,
    val serialNumber: Long,
    val price: Double
)
