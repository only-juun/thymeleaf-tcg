package com.splab.splabassignment.card.repository

import com.splab.splabassignment.card.dto.response.CardResponse

interface CardQueryRepository {

    fun loadCardListByMemberId(memberId: Long) : List<CardResponse>
}