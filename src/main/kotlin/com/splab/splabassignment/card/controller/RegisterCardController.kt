package com.splab.splabassignment.card.controller

import com.splab.splabassignment.card.dto.request.CreateCardCommand
import com.splab.splabassignment.card.service.usecase.CreateCardUseCase
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/cards")
class RegisterCardController(
    private val createCardUseCase: CreateCardUseCase,
) {
    @GetMapping("/registerForm")
    fun cardRegisterForm(@RequestParam("memberId") memberId: Long, model: Model) : String {
        val cardRegisterFormData = CreateCardCommand(game = null, title = null, serialNumber = null, price = null, memberId = memberId)
        model.addAttribute("cardRegisterFormData", cardRegisterFormData)
        return "registerCardForm"
    }

    @PostMapping("/register")
    fun submitCard(@Valid @ModelAttribute("cardRegisterFormData") cardFormData: CreateCardCommand,
                     bindingResult: BindingResult, model: Model) : String {

        if (!createCardUseCase.isSerialNumberAvailable(cardFormData.serialNumber, cardFormData.game))
            bindingResult.rejectValue("serialNumber", "error.cardRegisterFormData", "게임 내에서 이미 사용 중인 일련번호입니다.")

        if (bindingResult.hasErrors()) {
            model.addAttribute("memberId", cardFormData.memberId)
            return "registerCardForm"
        }

        createCardUseCase.createCard(cardFormData)
        return "redirect:/members/${cardFormData.memberId}"
    }
}