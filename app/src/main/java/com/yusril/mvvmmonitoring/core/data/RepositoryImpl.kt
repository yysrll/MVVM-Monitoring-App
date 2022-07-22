package com.yusril.mvvmmonitoring.core.data

import com.yusril.mvvmmonitoring.core.data.remote.MonitoringApi
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import com.yusril.mvvmmonitoring.utils.DataMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: MonitoringApi
) : MainRepository {
    override suspend fun getListStudent(nidn: String): StateFlow<Resource<List<Student>>> {
        val result = MutableStateFlow<Resource<List<Student>>>(Resource.empty())
        result.value = Resource.loading()
        try {
            val response = api.getStudent(nidn)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val listStudent = DataMapper.mapStudentResponseToStudent(responseBody.mahasiswas)
                result.value = Resource.success(listStudent)
            } else {
                    result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }
        return result
    }
}