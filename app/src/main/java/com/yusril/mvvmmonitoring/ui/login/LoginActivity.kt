package com.yusril.mvvmmonitoring.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yusril.mvvmmonitoring.databinding.ActivityLoginBinding
import com.yusril.mvvmmonitoring.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnLogin.setOnClickListener {
            MainActivity.start(this)
            finish()
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}