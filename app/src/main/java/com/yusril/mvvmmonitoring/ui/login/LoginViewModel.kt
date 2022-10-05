package com.yusril.mvvmmonitoring.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.model.Lecturer
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>>(Resource.empty())
    val loginState : StateFlow<Resource<String>> = _loginState

    var token: String = ""

    fun login(nidn: String, password: String) {
        getToken()
        _loginState.value = Resource.loading()
        val job = viewModelScope.launch {
            repository.login(nidn, password)
        }
        viewModelScope.launch(Dispatchers.IO) {
            job.join()
            Log.d("LoginViewModel", "getProfile")
            _loginState.value = repository.getProfile(token).value
            Log.d("LoginViewModel", "getProfile end")
        }
    }

    fun getToken() {
        viewModelScope.launch {
            Log.d("LoginViewModel", "getToken start")
            repository.getToken().collect {
                token = it
            }
            Log.d("LoginViewModel", "getToken end")
        }
    }
}