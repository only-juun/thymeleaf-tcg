package com.splab.splabassignment.card.repository

import com.splab.splabassignment.card.entity.Card
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CardRepository : JpaRepository<Card, Long>{
    @Query(" SELECT c " +
            "FROM Card c " +
            "WHERE 1 = 1 " +
            "AND c.serialNumber = :serialNumber " +
            "AND c.game = :game " +
            "AND c.deletedAt IS NULL ")
    fun findBySerialNumberAndGame(
        @Param("serialNumber") serialNumber: Long,
        @Param("game") game: String
    ): Card?

    @Query( "SELECT c " +
            "FROM Card c " +
            "WHERE 1 = 1 " +
            "AND c.cardId = :cardId " +
            "AND c.deletedAt IS NULL ")
    fun findByIdAndNotDeleted(@Param("cardId") cardId: Long): Card?
}