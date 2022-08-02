package com.yusril.mvvmmonitoring.core.data.remote

import com.yusril.mvvmmonitoring.core.data.remote.models.GetStudentProfileResponse
import com.yusril.mvvmmonitoring.core.data.remote.models.ListStudentResponse
import com.yusril.mvvmmonitoring.core.data.remote.models.ListStudyResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MonitoringApi {

    @GET("service/list-mahasiswa-bimbingan")
    suspend fun getStudent(
        @Query("nidn") nidn: String
    ): Response<ListStudentResponse>

    @GET("service/hasil-studi-mahasiswa")
    suspend fun getStudyResult(
        @Query("nim") nim: String,
        @Query("kode_semester") semester_code: String? = null
    ): Response<ListStudyResultResponse>

    @GET("service/mahasiswa")
    suspend fun getStudentDetail(
        @Query("nim") nim: String
    ): Response<GetStudentProfileResponse>
}