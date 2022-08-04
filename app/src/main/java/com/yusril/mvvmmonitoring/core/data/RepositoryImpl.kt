package com.yusril.mvvmmonitoring.core.data

import com.yusril.mvvmmonitoring.core.data.remote.MonitoringApi
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.domain.model.StudentProfile
import com.yusril.mvvmmonitoring.core.domain.model.StudyResult
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
        try {
            val response = api.getStudent(nidn)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val listStudent = DataMapper.mapStudentResponseToStudent(responseBody.mahasiswas)
                result.value = Resource.success(listStudent)
            } else if (responseBody == null) {
                result.value = Resource.empty()
            } else {
                    result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }
        return result
    }

    override suspend fun getStudyResult(nim: String, semester_code: String?): StateFlow<Resource<List<StudyResult>>> {
        val result = MutableStateFlow<Resource<List<StudyResult>>>(Resource.empty())
        try {
            val response = api.getStudyResult(nim, semester_code)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val listStudyResult = DataMapper.mapStudyResultResponseToStudyResult(responseBody.kartu_hasil_studi)
                result.value = Resource.success(listStudyResult)
            } else if (responseBody == null) {
                result.value = Resource.empty()
            } else {
                result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }

        return result
    }

    override suspend fun getStudentProfile(nim: String): StateFlow<Resource<StudentProfile>> {
        val result = MutableStateFlow<Resource<StudentProfile>>(Resource.empty())
        try {
            val response = api.getStudentDetail(nim)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val studentProfile = DataMapper.mapStudentProfileResponseToStudentProfile(responseBody.mahasiswa)
                result.value = Resource.success(studentProfile)
            } else if (responseBody == null) {
                result.value = Resource.empty()
            } else {
                result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }

        return result
    }

    override suspend fun login(nidn: String, password: String): StateFlow<Resource<String>> {
        val result = MutableStateFlow<Resource<String>>(Resource.empty())
        try {
            val response = api.login(nidn, password)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                result.value = Resource.success(responseBody.token)
            } else if (responseBody == null) {
                result.value = Resource.empty()
            } else {
                result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }

        return result
    }
}