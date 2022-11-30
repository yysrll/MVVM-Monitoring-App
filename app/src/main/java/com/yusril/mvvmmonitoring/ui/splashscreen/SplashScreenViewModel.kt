package com.yusril.mvvmmonitoring.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun token(): String = runBlocking {
        repository.getToken()
    }
}