package com.splab.splabassignment.level.event

import com.splab.splabassignment.level.entity.Level

data class ModifyMemberLevelNotify(
    val memberName: String,
    val oldLevel: Level,
    val newLevel: Level
)