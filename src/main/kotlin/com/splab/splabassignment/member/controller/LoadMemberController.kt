package com.splab.splabassignment.member.controller

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.member.service.usecase.LoadMemberUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/members")
class LoadMemberController(
    private val loadMemberUseCase: LoadMemberUseCase,
) {

    @GetMapping("/list")
    fun loadMemberList(model: Model) : String {
        val memberList = loadMemberUseCase.loadMemberListByCondition(null, null)
        model.addAttribute("memberList", memberList)
        return "loadMemberList"
    }

    @GetMapping("/{memberId}")
    fun loadMemberDetails(@PathVariable memberId: Long, model: Model) : String {
        val member = loadMemberUseCase.loadMemberById(memberId)
            ?: throw IllegalArgumentException("회원를 찾을 수 없습니다. memberId: $memberId")
        val cardList = member.cards.filter { it.deletedAt == null }
        model.addAttribute("member", member)
        model.addAttribute("cardList", cardList)
        return "loadMemberDetails"
    }

    @GetMapping("/search")
    fun loadMemberByCondition(@RequestParam(required = false) level: Level?,
                              @RequestParam(required = false) name: String?,
                              model: Model) : String {
        val memberList = loadMemberUseCase.loadMemberListByCondition(name, level)
        model.addAttribute("memberList", memberList)
        return "/loadMemberList :: #table-container"
    }


}