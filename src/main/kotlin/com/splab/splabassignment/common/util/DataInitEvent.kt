package com.splab.splabassignment.common.util

import com.splab.splabassignment.level.entity.Level
import com.splab.splabassignment.game.entity.Game
import com.splab.splabassignment.game.repository.GameRepository
import com.splab.splabassignment.member.entity.Member
import com.splab.splabassignment.member.repository.MemberRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.random.Random

@Component
@Transactional
class DataInitEvent(
    private val gameRepository: GameRepository,
    private val memberRepository: MemberRepository,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun insertDummyUsersAndGames() {
        val memberList = mutableListOf<Member>()

        for (i in 1..30) {
            val name = generateRandomEnglishName()
            val memberResponse = Member(
                memberName = name,
                memberEmail = "${replaceSpaceWithUnderscore(name)}_${i}@splab.dev",
                joinDate = generateRandomDateWithinLastYear(),
                level = Level.BRONZE,
                cardCount = 0,
                cardTotalPrice = 0.0
            )

            memberList.add(memberResponse)
        }

        memberRepository.saveAll(memberList)

        val gameList = listOf(
            Game(gameName = "유희왕"),
            Game(gameName = "매직 더 게더링"),
            Game(gameName = "포켓몬")
        )

        gameRepository.saveAll(gameList)
    }

    private fun generateRandomEnglishName(): String {
        val firstNames = listOf("James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen")
        val lastNames = listOf("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson")

        val firstName = firstNames[Random.nextInt(firstNames.size)]
        val lastName = lastNames[Random.nextInt(lastNames.size)]

        return "$firstName $lastName"
    }

    private fun replaceSpaceWithUnderscore(name: String): String {
        return name.replace(" ", "_")
    }

    private fun generateRandomDateWithinLastYear(): LocalDate {
        val today = LocalDate.now()
        val oneYearAgo = today.minusYears(1)

        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(oneYearAgo, today)
        val randomDays = Random.nextLong(daysBetween + 1)

        return oneYearAgo.plusDays(randomDays)
    }

}