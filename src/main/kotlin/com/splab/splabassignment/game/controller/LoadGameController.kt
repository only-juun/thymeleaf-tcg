package com.splab.splabassignment.game.controller

import com.splab.splabassignment.game.repository.GameQueryRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoadGameController(
    private val gameQueryRepository: GameQueryRepository
) {
    @GetMapping("/games")
    fun loadGameList() =
        ResponseEntity.ok(gameQueryRepository.loadGameList())
}