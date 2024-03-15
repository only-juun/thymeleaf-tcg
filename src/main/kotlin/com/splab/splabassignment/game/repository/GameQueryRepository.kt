package com.splab.splabassignment.game.repository

interface GameQueryRepository {
    fun loadGameList() : List<String>
}