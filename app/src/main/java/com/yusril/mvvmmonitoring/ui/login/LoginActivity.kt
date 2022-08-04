package com.yusril.mvvmmonitoring.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.ActivityLoginBinding
import com.yusril.mvvmmonitoring.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etNip.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.loginState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "Loading")
                    }
                    Status.SUCCESS -> {
                        MainActivity.start(this@LoginActivity)
                        finish()
                        Log.d(TAG, "Success: token ${it.data}")
                    }
                    Status.EMPTY -> {
                        Log.d(TAG, "Empty")
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "Error: ${it.message}")
                    }
                }
            }
        }
    }

    companion object {
        val TAG = LoginActivity::class.java.simpleName
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}