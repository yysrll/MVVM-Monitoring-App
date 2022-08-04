package com.yusril.mvvmmonitoring.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.yusril.mvvmmonitoring.R
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
                        binding.btnLogin.visibility = View.INVISIBLE
                        binding.pbLogin.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        MainActivity.start(this@LoginActivity)
                        finish()
                        Log.d(TAG, "Success: token ${it.data}")
                    }
                    Status.EMPTY -> {
                        Log.d(TAG, "Empty")
                        binding.btnLogin.visibility = View.VISIBLE
                        binding.pbLogin.visibility = View.INVISIBLE
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "Error: ${it.message}")
                        binding.btnLogin.visibility = View.VISIBLE
                        binding.pbLogin.visibility = View.INVISIBLE
                        Snackbar.make(binding.root, it.message ?: "", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.danger_200))
                            .setTextColor(ContextCompat.getColor(applicationContext, R.color.danger_700))
                            .show()
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