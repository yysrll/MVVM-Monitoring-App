package com.yusril.mvvmmonitoring.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.model.Semester
import com.yusril.mvvmmonitoring.core.domain.model.StudentProfile
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

    private val _studentProfile = MutableStateFlow<Resource<StudentProfile>>(Resource.empty())
    val studentProfile : StateFlow<Resource<StudentProfile>> = _studentProfile

    private val _listSemester = MutableStateFlow<Resource<List<Semester>>>(Resource.empty())
    val listSemester : StateFlow<Resource<List<Semester>>> = _listSemester



    fun getListStudyGrade(nim: String, semester_code: String? = null) {
        _listStudyGrade.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _listStudyGrade.value = repository.getStudyResult(nim, semester_code).value
        }
    }

    fun getStudentProfile(nim: String) {
        _studentProfile.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _studentProfile.value = repository.getStudentProfile(nim).value
        }
    }

    fun getSemester() {
        _listSemester.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _listSemester.value = repository.getSemester().value
        }
    }
}