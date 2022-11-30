package com.yusril.mvvmmonitoring.core.domain.model

data class Krs(
    var isCurrent: Boolean,
    var isLocked: Boolean,
    var isApproved: Boolean,
    var listKrs: List<StudyResult>? = null
)
