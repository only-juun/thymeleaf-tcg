package com.splab.splabassignment.member.controller

import com.splab.splabassignment.member.dto.request.ModifyMemberCommand
import com.splab.splabassignment.member.service.usecase.ModifyMemberUseCase
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/members")
class ModifyMemberController(
    private val modifyMemberUseCase: ModifyMemberUseCase
) {

    @GetMapping("/modifyForm")
    fun memberModifyForm(@RequestParam("memberId") memberId: Long, model: Model): String {
        val memberModifyFormData = modifyMemberUseCase.loadTargetMember(memberId)
        model.addAttribute("memberModifyFormData", memberModifyFormData)
        return "modifyMemberForm"
    }

    @PostMapping("/modify")
    fun submitMember(@Valid @ModelAttribute("memberModifyFormData") memberFormData: ModifyMemberCommand,
                     bindingResult: BindingResult
    ) : String {

        if (!modifyMemberUseCase.isEmailAvailable(memberFormData.memberId, memberFormData.memberEmail ?: ""))
            bindingResult.rejectValue("memberEmail", "error.memberModifyFormData", "이미 사용 중인 이메일입니다.")

        if (bindingResult.hasErrors())
            return "modifyMemberForm"

        modifyMemberUseCase.modifyMember(memberFormData)
        return "redirect:/members/${memberFormData.memberId}"
    }
}