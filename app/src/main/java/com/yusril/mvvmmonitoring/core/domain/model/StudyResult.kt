package com.yusril.mvvmmonitoring.core.domain.model

data class StudyResult(
    val name: String,
    val scoreNumber: String = "",
    val scoreLetter: String = "",
    val sks: Int,
    val scoreTotal: String = "",
)
