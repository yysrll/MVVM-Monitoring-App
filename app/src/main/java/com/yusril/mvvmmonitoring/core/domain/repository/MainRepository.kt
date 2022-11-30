package com.yusril.mvvmmonitoring.core.domain.repository

import com.yusril.mvvmmonitoring.core.domain.model.*
import com.yusril.mvvmmonitoring.core.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    suspend fun getListStudent(token: String, nidn: String): Resource<List<Student>>
    suspend fun getStudyResult(token: String, nim: String, semester_code: String?): Resource<ListStudyResult>
    suspend fun getSemester(token: String): Resource<List<Semester>>
    suspend fun getKrs(token: String, studentId: Int): Resource<Krs>
    suspend fun approveKrs(token: String, krsId: Int): Resource<String>

    suspend fun login(nidn: String, password: String): Resource<String>
    suspend fun getProfile(token: String): Resource<String>

    suspend fun getToken(): String
    suspend fun getCurrentLecturer(): Lecturer
    suspend fun deleteLecturer()
}