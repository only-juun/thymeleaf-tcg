package com.splab.splabassignment.member.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.dto.response.MemberResponse
import com.splab.splabassignment.member.entity.QMember.member
import org.springframework.stereotype.Repository

@Repository
class MemberQueryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : MemberQueryRepository {
    override fun loadMemberListByCondition(name: String?, level: Level?) : List<MemberResponse> {
        return jpaQueryFactory.select(
            Projections.constructor(
                MemberResponse::class.java,
                member.memberId,
                member.memberName,
                member.memberEmail,
                member.joinDate,
                member.level,
                member.cardCount
            )).from(member)
            .where(
                levelEq(level), nameEq(name),
                isNotDeleted(),
            )
            .orderBy(member.joinDate.desc())
            .fetch()
    }

    private fun levelEq(level: Level?) =
        if (level != null) member.level.eq(level) else null

    private fun nameEq(name: String?) =
        if (!name.isNullOrBlank()) member.memberName.contains(name) else null

    private fun isNotDeleted(): BooleanExpression {
        return member.deletedAt.isNull
    }
}