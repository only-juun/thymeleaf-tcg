package com.splab.splabassignment.member.controller

import com.splab.splabassignment.member.dto.request.CreateMemberCommand
import com.splab.splabassignment.member.service.usecase.CreateMemberUseCase
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/members")
class CreateMemberController(
    private val createMemberUseCase: CreateMemberUseCase
) {
    @GetMapping("/registerForm")
    fun memberRegisterForm(model: Model) : String {
        val memberRegisterFormData = CreateMemberCommand(memberName = null, memberEmail = null, joinDate = null)
        model.addAttribute("memberRegisterFormData", memberRegisterFormData)
        return "registerMemberForm"
    }

    @PostMapping("/register")
    fun submitMember(@Valid @ModelAttribute("memberRegisterFormData") memberFormData: CreateMemberCommand,
                     bindingResult: BindingResult
    ) : String {
        if (bindingResult.hasErrors())
            return "registerMemberForm"

        createMemberUseCase.createMember(memberFormData)

        return "redirect:/members/list"
    }
}