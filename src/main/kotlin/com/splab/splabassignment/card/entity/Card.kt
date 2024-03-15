package com.splab.splabassignment.card.entity

import com.splab.splabassignment.common.entity.BaseTime
import com.splab.splabassignment.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Card(
    @Id
    @Column(name = "CARD_ID", columnDefinition = "LONG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var cardId: Long? = null,

    @Column(name = "GAME")
    val game: String?,

    @Column(name = "TITLE")
    val title: String,

    @Column(name = "SERIAL_NUMBER")
    val serialNumber: Long,

    @Column(name = "PRICE")
    val price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    val member: Member,

    @Column(name = "DELETED_AT", columnDefinition = "TIMESTAMP")
    var deletedAt: LocalDateTime? = null
) : BaseTime() {
    fun deleteCard(deletedAt: LocalDateTime) {
        this.deletedAt = deletedAt
    }
}

