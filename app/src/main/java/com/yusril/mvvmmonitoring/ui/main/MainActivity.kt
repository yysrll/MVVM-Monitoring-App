package com.yusril.mvvmmonitoring.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.StudentAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.ActivityMainBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity
import com.yusril.mvvmmonitoring.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentAdapter: StudentAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = ""
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        initRecyclerView()

        binding.tvGreeting.text = getString(R.string.greeting_messages, "Pak", "John")

//        val angkatan = arrayListOf("Semua", "2015", "2016", "2017", "2018", "2019", "2020", "2021")
//        angkatan.forEach {
//            addChip(it)
//        }

        viewModel.getStudent("1234567890")


        lifecycleScope.launchWhenCreated {
            viewModel.students.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "Loading")
                    }
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            studentAdapter.addStudents(data)
                        }
                        Log.d(TAG, "Success: ${it.data}")
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

    private fun initRecyclerView() {
        studentAdapter = StudentAdapter()
        val rv = binding.rvStudents
        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
            setHasFixedSize(true)
        }
        studentAdapter.setOnItemClickCallback(object : StudentAdapter.OnItemClickCallback {
            override fun onItemClicked(student: Student) {
                DetailActivity.start(this@MainActivity, student)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> LoginActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addChip(name: String) {
        val chip = Chip(this)
        chip.apply {
            text = name
            isCheckable = true
            isClickable = true
            isCheckedIconVisible = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Log.d("CHIP", name)
                }
            }
        }
        binding.chipGroup.addView(chip)
    }

    companion object {
        val TAG = MainActivity::class.simpleName
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}