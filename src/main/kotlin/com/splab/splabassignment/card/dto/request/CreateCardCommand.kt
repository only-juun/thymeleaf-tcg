package com.splab.splabassignment.card.dto.request

import com.splab.splabassignment.card.entity.Card
import com.splab.splabassignment.member.entity.Member
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.math.RoundingMode

data class CreateCardCommand (
    @field:NotNull
    val memberId: Long?,

    @field:NotBlank(message = "게임은 공백이면 안 됩니다")
    @field:Pattern(
        regexp = "^(매직 더 게더링|유희왕|포켓몬)$",
        message = "게임은 매직 더 게더링, 유희왕, 포켓몬 중 하나여야 합니다"
    )
    val game: String?,

    @field:NotBlank(message = "타이틀은 공백일 수 없습니다")
    @field:Size(min = 1, max = 100, message = "타이틀은 1자 이상 100자 이하이어야 합니다")
    val title: String?,

    @field:NotNull(message = "일련번호는 공백이면 안 됩니다")
    @field:Min(value = 1, message = "일련번호는 1 이상이어야 합니다")
    val serialNumber: Long?,

    @field:NotNull(message = "가격은 공백이면 안 됩니다")
    @field:DecimalMin(value = "0.00", message = "가격은 0 이상이어야 합니다")
    @field:DecimalMax(value = "10000.00", message = "가격은 10,000 이하이어야 합니다")
    val price: Double?,
) {
    fun mapToEntity(member: Member) = Card(
        game = game,
        title = title!!,
        serialNumber = serialNumber!!,
        price = BigDecimal(price!!).setScale(2, RoundingMode.HALF_UP).toDouble(),
        member = member
    )
}