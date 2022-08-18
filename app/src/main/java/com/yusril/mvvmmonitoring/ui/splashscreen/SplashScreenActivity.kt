package com.yusril.mvvmmonitoring.ui.splashscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yusril.mvvmmonitoring.databinding.ActivitySplashScreenBinding
import com.yusril.mvvmmonitoring.ui.login.LoginActivity
import com.yusril.mvvmmonitoring.ui.main.MainActivity
import com.yusril.mvvmmonitoring.utils.Constant.SPLASH_SCREEN_DURATION_IN_MILLIS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenCreated {
                viewModel.getCurrentLecturerLogin().collect { lecturer ->
                    if (lecturer.token == "") {
                        LoginActivity.start(this@SplashScreenActivity)
                    } else {
                        MainActivity.start(this@SplashScreenActivity, lecturer)
                    }
                    finish()
                }
            }
        }, SPLASH_SCREEN_DURATION_IN_MILLIS)
    }
}