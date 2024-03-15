package com.splab.splabassignment.level.service

import com.splab.splabassignment.card.entity.Card
import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.level.event.ModifyMemberLevelNotify
import com.splab.splabassignment.member.entity.Member
import com.splab.splabassignment.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

internal class UpdateLevelServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxUnitFun = true)
    val updateLevelService = UpdateLevelService(memberRepository, eventPublisher)
    val eventSlot = slot<ModifyMemberLevelNotify>()

    isolationMode = IsolationMode.InstancePerLeaf

    Given("존재하지 않는 회원에 대해") {
        val invalidMemberId = 999L

        every { memberRepository.findByIdAndNotDeleted(invalidMemberId) } returns null

        When("카드 정보를 업데이트하면") {
            val exception = shouldThrow<NoSuchElementException> {
                updateLevelService.updateMemberCardInfo(invalidMemberId)
            }

            Then("아무 일도 일어나지 않고, 예외를 던져야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(invalidMemberId) }
                verify(exactly = 0) { eventPublisher.publishEvent(any()) }
                exception.message shouldBe "멤버를 찾을 수 없습니다. memberId: $invalidMemberId"
            }
        }
    }

    Given("임의의 레벨을 가진 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.memberId } returns memberId

        val oldLevel = member.level

        When("임의의 게임 종류의 무료 게임 카드를 추가하면") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 0.0
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("레벨 변화가 일어나지 않아야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                member.level shouldBe oldLevel
            }
        }
    }

    Given("하나의 유효카드가 존재하는 SILVER 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf(
            mockk<Card>(relaxed = true) {
                every { price } returns 30.0
                every { game } returns "유희왕"
                every { deletedAt } returns null
            })

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.SILVER
        every { member.memberId } returns memberId

        When("카드를 삭제하여 카드 정보를 업데이트하면") {
            cards.removeAt(0)
            updateLevelService.updateMemberCardInfo(memberId)

            Then("회원 레벨은 SILVER 에서 BRONZE 로 변경되어야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }
                eventSlot.captured.apply {
                    newLevel shouldBe Level.BRONZE
                    oldLevel shouldBe Level.SILVER
                    memberName shouldBe member.memberName
                }
            }
        }
    }

    Given("하나의 유효 카드를 가진 SILVER 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf(
            mockk<Card>(relaxed = true) {
                every { price } returns 30.0
                every { game } returns "유희왕"
                every { deletedAt } returns null
            })

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.SILVER
        every { member.memberId } returns memberId

        When("다른 종류 게임의 카드를 추가하여 게임 종류는 2가지 이상이지만, 가격 합은 100 미만이라면") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 20.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("레벨 변화가 일어나지 않아야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 0) { eventPublisher.publishEvent(any()) }
            }
        }

        When("다른 종류 게임의 카드를 추가하여 게임 종류는 2가지 이상이고 가격 합이 100 이상이라면") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 80.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("SILVER에서 GOLD로 레벨이 변경되어야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.GOLD
                    oldLevel shouldBe Level.SILVER
                }
            }
        }
    }

    Given("하나의 무료 카드를 가진 BRONZE 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()

        // 가격이 0인 카드 설정
        val cardsWithZeroPrice = mutableListOf(
            mockk<Card>(relaxed = true) {
                every { price } returns 0.0
                every { game } returns "유희왕"
                every { deletedAt } returns null
            }
        )
        cards.addAll(cardsWithZeroPrice)

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.BRONZE
        every { member.memberId } returns memberId

        When("다른 종류의 20달러 유효 카드를 추가하면") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 20.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })

            updateLevelService.updateMemberCardInfo(memberId)

            Then("BRONZE에서 SILVER로 레벨이 변화해야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.SILVER
                    oldLevel shouldBe Level.BRONZE
                }
            }
        }

        When("다른 종류의 100 달러 유효 카드를 추가하면") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 100.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })

            updateLevelService.updateMemberCardInfo(memberId)

            Then("BRONZE에서 SILVER로 레벨이 변화해야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.SILVER
                    oldLevel shouldBe Level.BRONZE
                }
            }
        }

    }

    Given("하나의 유효 카드를 가지고 있는 SILVER 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()
        cards.add(mockk<Card>(relaxed = true) {
            every { price } returns 50.0
            every { game } returns "유희왕"
            every { deletedAt } returns null
        })

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.SILVER
        every { member.memberId } returns memberId

        When("같은 게임 종류의 카드를 추가하여 가격 총합이 100인 경우") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 50.0
                every { game } returns "유희왕"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("레벨 변화가 일어나지 않아야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 0) { eventPublisher.publishEvent(any()) }
                member.level shouldBe Level.SILVER
            }
        }

        When("다른 게임 종류의 카드를 추가하여 가격 총합이 100인 경우") {
            cards.add(mockk<Card>(relaxed = true) {
                every { price } returns 50.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("SILVER에서 GOLD로 레벨이 변경되어야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.GOLD
                    oldLevel shouldBe Level.SILVER
                }
            }
        }
    }

    Given("총합이 100 미만인 세 개의 서로 다른 게임의 카드를 가지고 있는 SILVER 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()

        cards.add(mockk(relaxed = true) {
            every { price } returns 30.0
            every { game } returns "유희왕"
            every { deletedAt } returns null
        })
        cards.add(mockk(relaxed = true) {
            every { price } returns 25.0
            every { game } returns "매직 더 게더링"
            every { deletedAt } returns null
        })
        cards.add(mockk(relaxed = true) {
            every { price } returns 10.0
            every { game } returns "포켓몬"
            every { deletedAt } returns null
        })

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.SILVER
        every { member.memberId } returns memberId

        When("무료 카드를 추가하면") {
            cards.add(mockk(relaxed = true) {
                every { price } returns 0.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("레벨 변화가 일어나지 않아야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                member.level shouldBe Level.SILVER
                verify(exactly = 0) { eventPublisher.publishEvent(capture(eventSlot)) }
            }
        }

        When("유효 카드를 추가하면") {
            cards.add(mockk(relaxed = true) {
                every { price } returns 10.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
            updateLevelService.updateMemberCardInfo(memberId)

            Then("SILVER에서 GOLD로 레벨이 변경되어야 한다") {
                verify(exactly = 1) { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.GOLD
                    oldLevel shouldBe Level.SILVER
                }
            }
        }
    }

    Given("총합이 100미만인 네 개의 서로 다른 게임의 카드를 가지고 있는 GOLD 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()

        repeat(3) {
            cards.add(mockk(relaxed = true) {
                every { price } returns 20.0
                every { game } returns "유희왕"
                every { deletedAt } returns null
            })
        }

        repeat(1) {
            cards.add(mockk(relaxed = true) {
                every { price } returns 20.0
                every { game } returns "포켓몬"
                every { deletedAt } returns null
            })
        }

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } returns Level.GOLD
        every { member.memberId } returns memberId

        When("카드 종류가 하나가 되도록 카드를 삭제하면") {
            cards.removeAt(3)
            updateLevelService.updateMemberCardInfo(memberId)

            Then("GOLD에서 SILVER로 레벨이 변경되어야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.SILVER
                    oldLevel shouldBe Level.GOLD
                }
            }
        }
    }

    Given("총합이 100이상인 두 종류의 유효카드를 가진 GOLD 회원에 대해") {
        val memberId = 1L
        val member = mockk<Member>(relaxed = true)
        val cards = mutableListOf<Card>()

        // 서로 다른 게임의 2개 카드 설정, 총 가격은 100 이상
        cards.add(mockk(relaxed = true) {
            every { price } returns 60.0
            every { game } returns "Game A"
            every { deletedAt } returns null
        })
        cards.add(mockk(relaxed = true) {
            every { price } returns 50.0
            every { game } returns "Game B"
            every { deletedAt } returns null
        })

        every { memberRepository.findByIdAndNotDeleted(memberId) } returns member
        every { member.cards } returns cards
        every { member.level } answers { Level.GOLD }
        every { member.memberId } returns memberId

        When("하나의 카드를 삭제하면") {
            cards.removeAt(0)
            updateLevelService.updateMemberCardInfo(memberId)

            Then("GOLD에서 SILVER로 레벨이 변경되어야 한다") {
                verify { memberRepository.findByIdAndNotDeleted(memberId) }
                verify(exactly = 1) { eventPublisher.publishEvent(capture(eventSlot)) }

                eventSlot.captured.apply {
                    newLevel shouldBe Level.SILVER
                    oldLevel shouldBe Level.GOLD
                }
            }
        }
    }
})