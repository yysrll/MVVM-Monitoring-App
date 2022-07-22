package com.yusril.mvvmmonitoring.core.data.remote

import com.yusril.mvvmmonitoring.core.data.remote.models.ListStudentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MonitoringApi {

    @GET("service/list-mahasiswa-bimbingan")
    suspend fun getStudent(
        @Query("nidn") nidn: String
    ): Response<ListStudentResponse>
}