package com.yusril.mvvmmonitoring.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: MainRepository
    ): ViewModel() {

    private val _students = MutableStateFlow<Resource<List<Student>>>(Resource.loading())
    val students: StateFlow<Resource<List<Student>>> = _students

    fun getStudent(token: String, nidn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _students.value = repository.getListStudent(token, nidn).value
        }
    }

    fun getLecturer() = repository.getCurrentLecturer()

    fun deleteLecturerLogin() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteLecturer()
    }
}