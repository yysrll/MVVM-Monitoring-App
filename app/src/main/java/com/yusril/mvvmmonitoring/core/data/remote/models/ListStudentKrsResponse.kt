package com.yusril.mvvmmonitoring.core.data.remote.models

import com.google.gson.annotations.SerializedName

data class ListStudentKrsResponse(
	val mahasiswas: List<MahasiswasItem>
)

data class CurrentKartuRencanaStudi(
	val id: Int,
	@SerializedName("is_current")
	val isCurrent: Int,
	@SerializedName("is_locked")
	val isLocked: Int,
	@SerializedName("is_approved")
	val isApproved: Int,
)

data class MahasiswasItem(
	val nim: String,
	@SerializedName("nama_mahasiswa")
	val namaMahasiswa: String,
	val angkatan: String,
	@SerializedName("current_kartu_rencana_studi")
	val currentKartuRencanaStudi: CurrentKartuRencanaStudi
)

