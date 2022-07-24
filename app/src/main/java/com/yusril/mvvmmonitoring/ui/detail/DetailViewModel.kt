package com.yusril.mvvmmonitoring.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.model.StudyResult
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

    private val _listStudyGrade = MutableStateFlow<Resource<List<StudyResult>>>(Resource.empty())
    val listStudyGrade : StateFlow<Resource<List<StudyResult>>> = _listStudyGrade


    fun getListStudyGrade(nim: String, semester_code: String? = null) {
        _listStudyGrade.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _listStudyGrade.value = repository.getStudyResult(nim, semester_code).value
        }
    }
}