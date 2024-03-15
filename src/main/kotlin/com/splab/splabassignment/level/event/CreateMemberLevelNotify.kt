package com.splab.splabassignment.level.event

import com.splab.splabassignment.level.entity.Level

data class CreateMemberLevelNotify(
    val memberName: String,
    val newLevel: Level
)