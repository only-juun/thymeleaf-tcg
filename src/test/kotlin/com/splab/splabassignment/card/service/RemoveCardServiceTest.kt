package com.splab.splabassignment.card.service

import com.splab.splabassignment.card.entity.Card
import com.splab.splabassignment.card.repository.CardRepository
import com.splab.splabassignment.level.service.usecase.UpdateLevelUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

internal class RemoveCardServiceTest : BehaviorSpec({

    val cardRepository = mockk<CardRepository>()
    val updateLevelUseCase = mockk<UpdateLevelUseCase>(relaxUnitFun = true)
    val removeCardService = RemoveCardService(cardRepository, updateLevelUseCase)

    isolationMode = IsolationMode.InstancePerLeaf

    Given("카드 삭제를 위해") {
        val cardId = 1L
        val memberId = 1L
        val card = mockk<Card>(relaxed = true)

        every { cardRepository.findByIdAndNotDeleted(cardId) } returns card

        When("존재하는 카드 정보가 주어졌을 때") {
            removeCardService.removeCard(cardId, memberId)

            Then("카드 삭제가 이루어지고 레벨 갱신이 일어나야 한다") {
                verify { card.deleteCard(any()) }
                verify { updateLevelUseCase.updateMemberCardInfo(memberId) }
            }
        }
    }

    Given("존재하지 않는 카드 정보가 주어졌을 때") {
        val invalidCardId = 999L
        val memberId = 1L

        every { cardRepository.findByIdAndNotDeleted(invalidCardId) } returns null

        When("removeCard 메서드가 호출되었을 때") {
            Then("NoSuchElementException 예외가 던져져야 한다") {
                shouldThrow<NoSuchElementException> {
                    removeCardService.removeCard(invalidCardId, memberId)
                }
            }
        }
    }

})
