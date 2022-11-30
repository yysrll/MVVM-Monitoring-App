package com.yusril.mvvmmonitoring.core.data.remote.models

import com.google.gson.annotations.SerializedName

data class ListKrsResponse(
	@SerializedName("kartu_rencana_studi")
	val kartuRencanaStudi: KartuRencanaStudi
)

data class MataKuliahJumlahSksesItem(
	@SerializedName("jumlah_sks")
	val jumlahSks: Int,
	@SerializedName("id_tipe_sks")
	val idTipeSks: Int,
)

data class MataKuliah(
	@SerializedName("nama_resmi")
	val namaResmi: String,
	@SerializedName("id_kelas_kuliah_jenis")
	val idKelasKuliahJenis: Int,
	@SerializedName("mata_kuliah_jumlah_skses")
	val mataKuliahJumlahSkses: List<MataKuliahJumlahSksesItem>,
)

data class KelasKuliahsItem(
	val nama: String,
	@SerializedName("mata_kuliah")
	val mataKuliah: MataKuliah
)

data class KartuRencanaStudi(
	val id: Int? = null,
	@SerializedName("is_current")
	val isCurrent: Int,
	@SerializedName("is_locked")
	val isLocked: Int,
	@SerializedName("is_approved")
	val isApproved: Int,
	@SerializedName("kelas_kuliahs")
	val kelasKuliahs: List<KelasKuliahsItem>,
)

