package com.yusril.mvvmmonitoring.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.databinding.ActivityMainBinding
import com.yusril.mvvmmonitoring.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = ""
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.tvGreeting.text = getString(R.string.greeting_messages, "Pak", "John")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> LoginActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}