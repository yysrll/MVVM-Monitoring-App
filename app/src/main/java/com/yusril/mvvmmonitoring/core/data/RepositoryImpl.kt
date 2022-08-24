package com.yusril.mvvmmonitoring.core.data

import android.util.Log
import com.yusril.mvvmmonitoring.core.data.local.PreferenceDataSource
import com.yusril.mvvmmonitoring.core.data.remote.MonitoringApi
import com.yusril.mvvmmonitoring.core.domain.model.*
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import com.yusril.mvvmmonitoring.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: MonitoringApi,
    private val local: PreferenceDataSource
) : MainRepository {
    override suspend fun getListStudent(nidn: String): StateFlow<Resource<List<Student>>> {
        val result = MutableStateFlow<Resource<List<Student>>>(Resource.empty())
        try {
            val response = api.getStudent(nidn)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.mahasiswas!!.isNotEmpty()) {
                    val listStudent = DataMapper.mapStudentResponseToStudent(responseBody.mahasiswas)
                    result.value = Resource.success(listStudent)
                } else {
                    result.value = Resource.empty()
                }
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
            if (response.isSuccessful) {
                if (responseBody?.kartu_hasil_studi!!.isNotEmpty()) {
                    val listStudyResult = DataMapper.mapStudyResultResponseToStudyResult(responseBody.kartu_hasil_studi)
                    result.value = Resource.success(listStudyResult)
                } else {
                    result.value = Resource.empty()
                }
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
            if (response.isSuccessful) {
                if (responseBody != null) {
                    val studentProfile = DataMapper.mapStudentProfileResponseToStudentProfile(responseBody.mahasiswa)
                    result.value = Resource.success(studentProfile)
                } else {
                    result.value = Resource.empty()
                }
            } else {
                result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }

        return result
    }

    override suspend fun getSemester(): StateFlow<Resource<List<Semester>>> {
        val result = MutableStateFlow<Resource<List<Semester>>>(Resource.empty())
        try {
            val response = api.getSemester()
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody != null) {
                    result.value = Resource.success(
                        DataMapper.mapListSemesterResponseToListSemester(responseBody.semesters)
                    )
                } else {
                    result.value = Resource.empty()
                }
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
            if (response.isSuccessful) {
                if (responseBody != null) {
                    result.value = Resource.success(responseBody.token)
                } else {
                    result.value = Resource.empty()
                }
            } else {
                result.value = Resource.error(response.message())
            }
        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "Something went wrong")
        }

        return result
    }

    override fun getCurrentLecturer(): Flow<Lecturer> = local.getCurrentUser()

    override suspend fun setNewLecturer(lecturer: Lecturer) = local.setNewLecturer(lecturer)

    override suspend fun deleteLecturer() = local.deleteLecturer()
}