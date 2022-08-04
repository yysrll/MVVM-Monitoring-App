package com.yusril.mvvmmonitoring.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import com.yusril.mvvmmonitoring.core.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>>(Resource.empty())
    val loginState : StateFlow<Resource<String>> = _loginState

    fun login(nidn: String, password: String) {
        _loginState.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.value = repository.login(nidn, password).value
        }
    }
}