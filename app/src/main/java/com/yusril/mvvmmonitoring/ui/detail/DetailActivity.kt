package com.yusril.mvvmmonitoring.ui.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SectionsPagerAdapter
import com.yusril.mvvmmonitoring.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var student: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.detail_title)
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDisplayHomeAsUpEnabled(true)
        }

        student = intent.getParcelableExtra(EXTRA_STUDENT)!!
        binding.detailStudentImage.setImageResource(R.drawable.ic_generic_avatar)
        binding.detailStudentNim.text = student.nim
        binding.detailStudentGpa.text = student.gpa

        setupPagerAdapter()

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