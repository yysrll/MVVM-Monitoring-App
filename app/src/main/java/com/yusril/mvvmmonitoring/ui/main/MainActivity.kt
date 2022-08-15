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
import androidx.appcompat.app.AlertDialog
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentListBinding: StudentListBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var lecturer: Lecturer
    private lateinit var errorAlertDialog: AlertDialog
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        studentListBinding = StudentListBinding.bind(binding.root)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = ""
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.primary_500)))
        }

        lecturer = intent.getParcelableExtra<Lecturer>(LECTURER) as Lecturer
        initRecyclerView()

        binding.tvLecturerName.text = getString(R.string.lecturer_name, lecturer.name)

        viewModel.getStudent(lecturer.nidn)


        lifecycleScope.launchWhenCreated {
            viewModel.students.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "Loading")
                        showLoading(true)
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        it.data?.let { data ->
                            studentAdapter.addStudents(data)
                        }
                        Log.d(TAG, "Success: ${it.data}")
                    }
                    Status.EMPTY -> {
                        showLoading(false)
                        Log.d(TAG, "Empty")
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        errorAlertDialog.show()
                        Log.d(TAG, "Error: ${it.message}")
                    }
                }
            }
        }

        errorAlertDialog = MyAlertDialog().setErrorAlertDialog(
            this,
            resources.getString(R.string.error_dialog_title),
            resources.getString(R.string.error_dialog_description),
            resources.getString(R.string.try_again)
        ) {
            viewModel.getStudent(lecturer.nidn)
        }
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
        val TAG = MainActivity::class.simpleName
        const val LECTURER = "lecturer"
        fun start(activity: Activity, lecturer: Lecturer) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(LECTURER, lecturer)
            activity.startActivity(intent)
        }
    }
}