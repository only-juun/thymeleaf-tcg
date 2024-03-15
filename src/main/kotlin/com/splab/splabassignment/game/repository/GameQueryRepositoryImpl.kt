package com.splab.splabassignment.game.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.splab.splabassignment.game.entity.QGame.game
import org.springframework.stereotype.Repository

@Repository
class GameQueryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : GameQueryRepository {
    override fun loadGameList(): List<String> =
        jpaQueryFactory.select(game.gameName)
            .from(game)
            .fetch()
}