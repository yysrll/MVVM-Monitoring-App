package com.yusril.mvvmmonitoring.core.domain.repository

import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.vo.Resource
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    suspend fun getListStudent(nidn: String): StateFlow<Resource<List<Student>>>
}