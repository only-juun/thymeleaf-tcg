package com.splab.splabassignment.card.controller

import com.splab.splabassignment.card.service.usecase.RemoveCardUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cards")
class RemoveCardController(
    private val removeCardUseCase: RemoveCardUseCase,
) {

    @DeleteMapping("/{cardId}")
    fun removeMember(@PathVariable("cardId") cardId: Long,
                     @RequestParam("memberId") memberId: Long) : String {
        removeCardUseCase.removeCard(cardId, memberId)
        return "redirect:/members/${memberId}"
    }
}