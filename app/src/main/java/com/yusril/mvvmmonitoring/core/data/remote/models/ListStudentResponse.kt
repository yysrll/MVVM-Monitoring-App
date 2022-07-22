package com.yusril.mvvmmonitoring.core.data.remote.models

data class ListStudentResponse(
    val mahasiswas: List<StudentResponse>,
    val message: String
)