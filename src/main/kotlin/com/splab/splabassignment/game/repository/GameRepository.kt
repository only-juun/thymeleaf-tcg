package com.splab.splabassignment.game.repository

import com.splab.splabassignment.game.entity.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long>