package com.yusril.mvvmmonitoring.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Lecturer
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.StudentAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.ActivityMainBinding
import com.yusril.mvvmmonitoring.databinding.StudentListBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity
import com.yusril.mvvmmonitoring.ui.login.LoginActivity
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentListBinding: StudentListBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var lecturer: Lecturer
    private lateinit var token: String
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTime = System.currentTimeMillis()
        binding = ActivityMainBinding.inflate(layoutInflater)
        studentListBinding = StudentListBinding.bind(binding.root)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            elevation = 0f
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.primary_500
                    )
                )
            )
        }

        initRecyclerView()

        token = intent.getStringExtra(TOKEN_EXTRA).toString()
        lecturer = viewModel.getLecturer()
        if (lecturer.nidn == "") {
            MyAlertDialog().setErrorAlertDialog(
                this@MainActivity,
                resources.getString(R.string.error_dialog_title),
                resources.getString(R.string.error_dialog_get_lecturer),
                resources.getString(R.string.login_again),
            ) {
                viewModel.deleteLecturerLogin()
                LoginActivity.start(this@MainActivity)
                finish()
            }.show()
        } else {
            binding.collapseToolbar.title = lecturer.name
            binding.includeHeader.tvLecturerName.text = lecturer.name
            binding.includeHeader.tvLecturerNidn.text = lecturer.nidn
            viewModel.getStudent(token, lecturer.nidn)
        }

        lifecycleScope.launch {
            showStudent()
        }

        Log.d("MainActivity Time: ", "execution time ${System.currentTimeMillis() - startTime} ms")
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getStudent(token, lecturer.nidn)
    }

    private suspend fun showStudent(){
        viewModel.students.collect {
            when (it.status) {
                Status.LOADING -> {
                    showLoading(true)
                }
                Status.SUCCESS -> {
                    showLoading(false)
                    showEmptyView(false)
                    it.data?.let { data ->
                        studentAdapter.addStudents(data)
                    }
                }
                Status.EMPTY -> {
                    showLoading(false)
                    showEmptyView(true)
                }
                Status.ERROR -> {
                    showLoading(false)
                    MyAlertDialog().setErrorAlertDialog(
                        this,
                        resources.getString(R.string.error_dialog_title),
                        it.message.toString(),
                        resources.getString(R.string.try_again)
                    ) {
                        viewModel.getStudent(token, lecturer.nidn)
                    }.show()
                }
            }
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        studentListBinding.emptyStatus.root.isInvisible = !isEmpty
    }

    private fun showLoading(isLoading: Boolean) {
        studentListBinding.rvStudents.isInvisible = isLoading
        studentListBinding.progressBar.isInvisible = !isLoading
    }

    private fun initRecyclerView() {
        studentAdapter = StudentAdapter()
        val rv = studentListBinding.rvStudents
        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
            setHasFixedSize(true)
        }
        studentAdapter.setOnItemClickCallback(object : StudentAdapter.OnItemClickCallback {
            override fun onItemClicked(student: Student) {
                DetailActivity.start(this@MainActivity, student, token)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                MyAlertDialog().setActionAlertDialog(
                    this,
                    resources.getString(R.string.logout_confirm),
                    resources.getString(R.string.logout),
                ) {
                    viewModel.deleteLecturerLogin()
                    LoginActivity.start(this)
                    finish()
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        private const val TOKEN_EXTRA = "TOKEN"
        fun start(activity: Activity, token: String) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            activity.startActivity(intent)
        }
    }
}