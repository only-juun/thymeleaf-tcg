package com.splab.splabassignment.card.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.splab.splabassignment.card.dto.response.CardResponse
import com.splab.splabassignment.card.entity.QCard.card
import com.splab.splabassignment.member.entity.QMember.member
import org.springframework.stereotype.Repository

@Repository
class CardQueryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : CardQueryRepository {
    override fun loadCardListByMemberId(memberId: Long): List<CardResponse> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CardResponse::class.java,
                card.cardId,
                card.game,
                card.title,
                card.serialNumber,
                card.price,
            )).from(card)
            .where(
                memberIdEq(memberId),
                isNotDeleted()
            )
            .fetch()
    }

    private fun memberIdEq(memberId: Long) =
        member.memberId.eq(memberId)

    private fun isNotDeleted(): BooleanExpression {
        return card.deletedAt.isNull
    }
}