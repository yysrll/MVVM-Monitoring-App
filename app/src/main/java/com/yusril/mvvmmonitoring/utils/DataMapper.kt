package com.yusril.mvvmmonitoring.utils

import com.yusril.mvvmmonitoring.core.data.remote.models.StudentResponse
import com.yusril.mvvmmonitoring.core.domain.model.Student

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
}