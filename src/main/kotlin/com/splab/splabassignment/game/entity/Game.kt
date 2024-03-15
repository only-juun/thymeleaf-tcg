package com.splab.splabassignment.game.entity

import com.splab.splabassignment.common.entity.BaseTime
import jakarta.persistence.*

@Entity
class Game(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_ID", columnDefinition = "LONG")
    val gameId: Long? = null,

    @Column(name = "GAME_NAME", columnDefinition = "VARCHAR(100)")
    val gameName: String
) : BaseTime()