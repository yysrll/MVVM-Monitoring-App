package com.yusril.mvvmmonitoring.utils

import com.yusril.mvvmmonitoring.core.data.remote.models.*
import com.yusril.mvvmmonitoring.core.domain.model.*

object DataMapper {

    fun mapStudentResponseToStudent(input: List<StudentResponse>) : List<Student> {
        val listStudent = ArrayList<Student>()
        input.map {
            val student = Student(
                id = it.id_mahasiswa,
                nim = it.nim,
                gpa = it.ipk,
                sks = it.jumlah_sks
            )
            listStudent.add(student)
        }
        return listStudent
    }

    fun mapStudyResultResponseToStudyResult(input: List<StudyResultResponse>) : List<StudyResult> {
        val listStudyResult = ArrayList<StudyResult>()
        input.map {
            val studyResult = StudyResult(
                name = it.nama_mata_kuliah,
                scoreLetter = it.nilai_huruf,
                scoreNumber = it.nilai_angka,
                sks = it.sks,
                scoreTotal = it.total_nilai
            )
            listStudyResult.add(studyResult)
        }
        return listStudyResult
    }

    fun mapListSemesterResponseToListSemester(list: List<SemesterResponse>) : List<Semester> {
        val listSemester = ArrayList<Semester>()
        list.map {
            val semester = Semester(
                jenis = it.jenis,
                kode = it.kode,
                tahun = it.tahun,
                tahunAjaran = it.tahun_ajaran,
            )
            listSemester.add(semester)
        }
        return listSemester
    }

    fun mergeListStudentKrsResponseToListStudent(
        listStudent: List<Student>,
        response: ListStudentKrsResponse
    ): List<Student> {

        for (i in listStudent.indices) {
            val resItem = response.mahasiswas.find {
                it.nim == listStudent[i].nim
            }
            listStudent[i].name = resItem?.namaMahasiswa
            listStudent[i].year = resItem?.angkatan
            listStudent[i].krsId = resItem?.currentKartuRencanaStudi?.id
            listStudent[i].krsIsCurrent = resItem?.currentKartuRencanaStudi?.isCurrent == 1
            listStudent[i].krsIsLocked = resItem?.currentKartuRencanaStudi?.isLocked == 1
            listStudent[i].krsIsApproved = resItem?.currentKartuRencanaStudi?.isApproved == 1
        }

        return listStudent
    }

    fun mapListKrsResponseToKrs(input: ListKrsResponse) : Krs {
        val listKrs = ArrayList<StudyResult>()
        input.kartuRencanaStudi.kelasKuliahs.map {
            val sks = it.mataKuliah.mataKuliahJumlahSkses.find { matkul ->
                matkul.idTipeSks == it.mataKuliah.idKelasKuliahJenis
            }
            sks?.jumlahSks?.let { total_sks ->
                listKrs.add(
                    StudyResult(
                        name = it.mataKuliah.namaResmi,
                        sks = total_sks
                    )
                )
            }
        }
        return Krs(
            isCurrent = input.kartuRencanaStudi.isCurrent == 1,
            isLocked = input.kartuRencanaStudi.isLocked == 1,
            isApproved = input.kartuRencanaStudi.isApproved == 1,
            listKrs = listKrs
        )
    }
}