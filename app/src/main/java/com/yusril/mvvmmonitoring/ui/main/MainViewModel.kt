package com.yusril.mvvmmonitoring.ui.main

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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: MainRepository
    ): ViewModel() {

    private val _students = MutableStateFlow<Resource<List<Student>>>(Resource.loading())
    val students: StateFlow<Resource<List<Student>>> = _students

    fun getStudent(token: String, nidn: String) {
        _students.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _students.value = repository.getListStudent(token, nidn)
        }
    }

    fun getLecturer() = runBlocking {
        repository.getCurrentLecturer()
    }

    fun deleteLecturerLogin() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteLecturer()
    }
}