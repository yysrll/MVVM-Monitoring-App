package com.yusril.mvvmmonitoring.utils

import com.yusril.mvvmmonitoring.core.data.remote.models.ListStudyResultResponse
import com.yusril.mvvmmonitoring.core.data.remote.models.StudentResponse
import com.yusril.mvvmmonitoring.core.data.remote.models.StudyResultResponse
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.domain.model.StudyResult

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
                score_letter = it.nilai_huruf,
                score_number = it.nilai_angka,
                sks = it.sks,
                score_total = it.total_nilai
            )
            listStudyResult.add(studyResult)
        }
        return listStudyResult
    }
}