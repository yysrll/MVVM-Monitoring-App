package com.yusril.mvvmmonitoring.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.model.Krs
import com.yusril.mvvmmonitoring.core.domain.model.ListStudyResult
import com.yusril.mvvmmonitoring.core.domain.model.Semester
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _listStudyGrade = MutableStateFlow<Resource<ListStudyResult>>(Resource.loading())
    val listStudyGrade : StateFlow<Resource<ListStudyResult>> = _listStudyGrade

    private val _listSemester = MutableStateFlow<Resource<List<Semester>>>(Resource.loading())
    val listSemester : StateFlow<Resource<List<Semester>>> = _listSemester

    private val _listKrs = MutableStateFlow<Resource<Krs>>(Resource.loading())
    val listKrs : StateFlow<Resource<Krs>> = _listKrs

    private val _krsStatus = MutableStateFlow<Resource<String>>(Resource.loading())
    val krsStatus : StateFlow<Resource<String>> = _krsStatus

    fun getListStudyGrade(token: String, nim: String, semester_code: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _listStudyGrade.value = repository.getStudyResult(token, nim, semester_code)
        }
    }

    fun getSemester(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _listSemester.value = repository.getSemester(token)
        }
    }

    fun getKrs(token: String, studentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _listKrs.value = repository.getKrs(token, studentId)
        }
    }

    fun approveKrs(token: String, krsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _krsStatus.value = repository.approveKrs(token, krsId)
        }
    }
}