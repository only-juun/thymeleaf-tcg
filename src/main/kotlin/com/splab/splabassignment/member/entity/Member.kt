package com.splab.splabassignment.member.entity

import com.splab.splabassignment.card.entity.Card
import com.splab.splabassignment.common.entity.BaseTime
import com.splab.splabassignment.level.entity.Level
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Member(
    @Id
    @Column(name = "MEMBER_ID", columnDefinition = "LONG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long? = null,

    @Column(name = "NAME", columnDefinition = "VARCHAR(100)")
    var memberName: String,

    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)")
    var memberEmail: String,

    @Column(name = "CARD_COUNT", columnDefinition = "INT")
    var cardCount: Int,

    @Column(name = "CARD_TOTAL_PRICE", columnDefinition = "DOUBLE")
    var cardTotalPrice: Double,

    @Column(name = "LEVEL", columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    var level: Level,

    @Column(name = "JOIN_DATE", columnDefinition = "TIMESTAMP")
    var joinDate: LocalDate,

    @Column(name = "DELETED_AT", columnDefinition = "TIMESTAMP")
    var deletedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "member")
    val cards: List<Card> = mutableListOf()
) : BaseTime() {
    fun modifyMember(name: String, email: String, joinDate: LocalDate) {
        this.memberName = name
        this.memberEmail = email
        this.joinDate = joinDate
    }

    fun updateMemberCardInfo(cardCount: Int, cardTotalPrice: Double) {
        this.cardCount = cardCount
        this.cardTotalPrice = cardTotalPrice
    }

    fun updateLevel(level: Level) {
        this.level = level
    }

    fun deleteMember(deleteAt: LocalDateTime) {
        this.deletedAt = deleteAt
    }
}