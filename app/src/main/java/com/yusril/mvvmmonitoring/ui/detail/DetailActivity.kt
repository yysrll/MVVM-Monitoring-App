package com.yusril.mvvmmonitoring.ui.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SectionsPagerAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.ActivityDetailBinding
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var student: Student
    private lateinit var errorAlertDialog: AlertDialog
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTop)
        supportActionBar?.apply {
            elevation = 0f
            title = null
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDisplayHomeAsUpEnabled(true)
        }

        student = intent.getParcelableExtra(EXTRA_STUDENT)!!
//        binding.detailStudentImage.setImageResource(R.drawable.ic_generic_avatar)
        binding.detailStudentNim.text = student.nim
        binding.detailStudentGpa.text = student.gpa

        setupPagerAdapter()

        viewModel.getStudentProfile(student.nim)
        lifecycleScope.launchWhenCreated {
            viewModel.studentProfile.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "Loading")
                    }
                    Status.SUCCESS -> {
                        Log.d(TAG, "Success: ${it.data?.name}")
                        binding.detailStudentName.text = it.data?.name
                    }
                    Status.EMPTY -> {
                        Log.d(TAG, "Empty")
                    }
                    Status.ERROR -> {
                        errorAlertDialog.show()
                        Log.d(TAG, "Error: ${it.message}")

                    }
                }
            }
        }

        setErrorAlertDialog()
    }

    private fun setErrorAlertDialog() {
        errorAlertDialog = MyAlertDialog().setErrorAlertDialog(
            this,
            resources.getString(R.string.error_dialog_title),
            resources.getString(R.string.error_dialog_description),
            resources.getString(R.string.try_again)
        ) {
            viewModel.getStudentProfile(student.nim)
        }
    }

    private fun setupPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.detailViewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.detailTabs, binding.detailViewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        val TAG = DetailActivity::class.simpleName
        val TAB_TITLES = arrayOf(
            "KHS",
            "TRANSKRIP"
        )
        const val EXTRA_STUDENT = "extra_student"
        fun start(activity: Activity, student: Student) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(EXTRA_STUDENT, student)
            activity.startActivity(intent)
        }
    }
}