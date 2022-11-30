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
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: MonitoringApi,
    private val local: PreferenceDataSource
) : MainRepository {

    override suspend fun getListStudent(
        token: String,
        nidn: String
    ): Resource<List<Student>> {
        return try {
            val response = api.getStudent(token, nidn)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.mahasiswas!!.isNotEmpty()) {
                    val listStudent = DataMapper.mapStudentResponseToStudent(responseBody.mahasiswas)

                    val res = api.getStudentListKrs(token)
                    val resBody = res.body()
                    if (res.isSuccessful) {
                        if (resBody?.mahasiswas!!.isNotEmpty()) {
                            val list = DataMapper.mergeListStudentKrsResponseToListStudent(listStudent, resBody)
                            Resource.success(list)
                        } else {
                            Resource.empty()
                        }
                    } else {
                        Resource.error(res.message())
                    }
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }


        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getStudyResult(
        token: String,
        nim: String,
        semester_code: String?
    ): Resource<ListStudyResult> {
         return try {
            val response = api.getStudyResult(token, nim, semester_code)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.kartu_hasil_studi!!.isNotEmpty()) {
                    val listStudyResult = DataMapper.mapStudyResultResponseToStudyResult(responseBody.kartu_hasil_studi)
                    val sks = listStudyResult.sumOf { it.sks }
                    val scoreTotal = listStudyResult.sumOf { it.scoreTotal.toDouble() }
                    val gpa = (scoreTotal / sks)
                        .toBigDecimal()
                        .setScale(2, java.math.RoundingMode.HALF_UP)
                        .toDouble()
                    val subject = listStudyResult.size
                    Resource.success(
                        ListStudyResult(
                            totalSubject = subject,
                            totalSks = sks,
                            totalGpa = gpa,
                            subjects = listStudyResult
                        )
                    )
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }

    }

    override suspend fun getSemester(token: String): Resource<List<Semester>> {
        return try {
            val response = api.getSemester(token)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.semesters!!.isNotEmpty()) {
                    Resource.success(
                        DataMapper.mapListSemesterResponseToListSemester(responseBody.semesters)
                    )
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getKrs(token: String, studentId: Int): Resource<Krs> {
        return try {
            val response = api.getStudentKrs(token, studentId)
            val responseBody = response.body()

            if (response.isSuccessful) {
                if (responseBody?.kartuRencanaStudi?.kelasKuliahs!!.isNotEmpty()) {
                    Resource.success(
                        DataMapper.mapListKrsResponseToKrs(responseBody)
                    )
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun approveKrs(token: String, krsId: Int): Resource<String> {
        return try {
            val response = api.approveKrs(token, krsId)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.message!!.isNotEmpty()) {
                    Resource.success(responseBody.message)
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun login(nidn: String, password: String): Resource<String> {
        return try {
            val response = api.login(nidn, password)
            val responseBody = response.body()
            if (response.isSuccessful) {
                if (responseBody?.access_token!!.isNotEmpty()) {
                    local.setToken(responseBody.access_token)
                    Resource.success("Login successfully")
                } else {
                    Resource.empty()
                }
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getProfile(token: String): Resource<String> {
        return try {
            val response = api.getProfile(token)
            val responseBody = response.body()
            if (response.isSuccessful) {
                val lecturer = Lecturer(
                    responseBody!!.user.name,
                    responseBody.user.dosen.nip
                )
                local.setNewLecturer(lecturer)
                Resource.success("Get Profile Successfully")
            } else {
                Resource.error("get profile failed ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getToken(): String = local.getToken().first()
    override suspend fun getCurrentLecturer(): Lecturer = local.getCurrentUser().first()
    override suspend fun deleteLecturer() = local.deleteLecturer()
}