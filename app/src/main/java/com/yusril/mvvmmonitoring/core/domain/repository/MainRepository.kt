package com.yusril.mvvmmonitoring.core.domain.repository

import com.yusril.mvvmmonitoring.core.domain.model.Lecturer
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.domain.model.StudentProfile
import com.yusril.mvvmmonitoring.core.domain.model.StudyResult
import com.yusril.mvvmmonitoring.core.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    suspend fun getListStudent(nidn: String): StateFlow<Resource<List<Student>>>
    suspend fun getStudyResult(nim: String, semester_code: String?): StateFlow<Resource<List<StudyResult>>>
    suspend fun getStudentProfile(nim: String): StateFlow<Resource<StudentProfile>>
    suspend fun login(nidn: String, password: String): StateFlow<Resource<String>>

    fun getCurrentLecturer(): Flow<Lecturer>
    suspend fun setNewLecturer(lecturer: Lecturer)
    suspend fun deleteLecturer()
}