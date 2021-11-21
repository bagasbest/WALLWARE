package com.application.wallware.alarm

data class AlarmModel(
    var name: String? = null,
    var time: Int? = 0,
    var uid: String? = null,
    var category: String? = null,
)