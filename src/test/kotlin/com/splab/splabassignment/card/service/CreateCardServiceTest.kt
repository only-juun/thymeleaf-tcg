package com.splab.splabassignment.card.service

import com.splab.splabassignment.card.dto.request.CreateCardCommand
import com.splab.splabassignment.card.repository.CardRepository
import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.level.service.usecase.UpdateLevelUseCase
import com.splab.splabassignment.member.entity.Member
import com.splab.splabassignment.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDate

internal class CreateCardServiceTest : BehaviorSpec({

    val cardRepository = mockk<CardRepository>()
    val memberRepository = mockk<MemberRepository>()
    val updateLevelUseCase = mockk<UpdateLevelUseCase>(relaxUnitFun = true)
    val createCardService = CreateCardService(cardRepository, memberRepository, updateLevelUseCase)

    isolationMode = IsolationMode.InstancePerLeaf

    Given("카드 추가를 위해") {

        When("유효한 카드 정보가 주어졌을 때") {
            val memberId = 1L
            val validCreateCardCommand = CreateCardCommand(
                memberId = memberId,
                game = "매직 더 게더링",
                title = "Sample Title",
                serialNumber = 12345L,
                price = 500.0
            )

            val member = Member(
                memberId = memberId,
                memberName = "John Doe",
                memberEmail = "john@example.com",
                cardCount = 0,
                cardTotalPrice = 0.0,
                level = Level.BRONZE,
                joinDate = LocalDate.now()
            )

            every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
            every { cardRepository.save(any()) } returnsArgument 0

            Then("카드 추가가 이루어지고 레벨 갱신이 일어나야 한다") {
                createCardService.createCard(validCreateCardCommand)

                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { cardRepository.save(any()) }
                verify(exactly = 1) { updateLevelUseCase.updateMemberCardInfo(memberId) }
            }
        }

        When("유효하지 않은 카드 정보(없는 회원)가 주어졌을 때") {
            val invalidMemberId = 999L
            val invalidCreateCardCommand = CreateCardCommand(
                memberId = invalidMemberId,
                game = "유희왕",
                title = "Invalid Card",
                serialNumber = 54321L,
                price = 100.0
            )

            every { memberRepository.findByIdAndNotDeleted(invalidMemberId) } returns null

            Then("예외를 던지고, 카드 저장은 이루어지지 않아야 한다") {
                shouldThrow<IllegalArgumentException> {
                    createCardService.createCard(invalidCreateCardCommand)
                }

                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(invalidMemberId) }
                verify(exactly = 0) { cardRepository.save(any()) }
                verify(exactly = 0) { updateLevelUseCase.updateMemberCardInfo(invalidMemberId) }
            }
        }
    }
})
