package com.splab.splabassignment.level.service

import com.splab.splabassignment.level.event.CreateMemberLevelNotify
import com.splab.splabassignment.level.event.ModifyMemberLevelNotify
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.client.RestClient

@Component
class NotifyLevelEventListener(
    private val restClient: RestClient,
    @Value("\${slack.webhook-url}") private val webhookUrl: String,
) {

    @TransactionalEventListener(ModifyMemberLevelNotify::class)
    fun sendChangeNotification(dto: ModifyMemberLevelNotify) {
        val message = "${dto.memberName} 회원의 레벨이 ${dto.oldLevel} 에서 ${dto.newLevel} 로 변경되었습니다."
        sendSlackNotification(message)
    }

    @TransactionalEventListener(CreateMemberLevelNotify::class)
    fun sendFirstNotification(dto: CreateMemberLevelNotify) {
        val message = "${dto.memberName} 회원의 레벨이 ${dto.newLevel} 로 부여되었습니다."
        sendSlackNotification(message)
    }

    private fun sendSlackNotification(message: String) {
        restClient.post()
            .uri(webhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("text" to message))
            .retrieve()
            .toBodilessEntity()
    }
}