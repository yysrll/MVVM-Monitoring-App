package com.yusril.mvvmmonitoring.core.domain.model

data class ListStudyResult(
    var totalSubject: Int,
    var totalSks: Int,
    var totalGpa: Double,
    var subjects: List<StudyResult>
)
