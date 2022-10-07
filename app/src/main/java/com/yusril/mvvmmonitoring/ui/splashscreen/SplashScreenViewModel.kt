package com.yusril.mvvmmonitoring.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.yusril.mvvmmonitoring.core.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun token(): Flow<String> = repository.getToken()
}