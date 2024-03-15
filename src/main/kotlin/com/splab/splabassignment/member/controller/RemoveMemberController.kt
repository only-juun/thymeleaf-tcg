package com.splab.splabassignment.member.controller

import com.splab.splabassignment.member.service.usecase.RemoveMemberUseCase
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class RemoveMemberController(
    private val removeMemberUseCase: RemoveMemberUseCase
) {
    @DeleteMapping("/{memberId}")
    fun removeMember(@PathVariable("memberId") memberId: Long) : String {
        removeMemberUseCase.removeMember(memberId)
        return "/members/list"
    }
}