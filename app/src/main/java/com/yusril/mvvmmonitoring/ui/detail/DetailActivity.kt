package com.yusril.mvvmmonitoring.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SectionsPagerAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.ActivityDetailBinding
import com.yusril.mvvmmonitoring.ui.detail.krs.KrsFragment
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var student: Student
    private lateinit var token: String
    private val viewModel: DetailViewModel by viewModels()
    private val krsFragment = KrsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTime = System.currentTimeMillis()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        student = intent.getParcelableExtra(EXTRA_STUDENT)!!
        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        binding.collapseToolbar.title = student.name
        binding.detailStudentName.text = student.name
        binding.detailStudentNim.text = student.nim
        binding.detailStudentGpa.text = getString(R.string.total_ipk, student.gpa)

        setupPagerAdapter()

        binding.fabApprove.setOnClickListener {
            student.krsId?.let {
                showApproveAlertDialog(token, it)
            }
        }

        lifecycleScope.launch {
            viewModel.krsStatus.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        krsFragment.onKrsApproved()
                        student.krsIsApproved = true
                        binding.fabApprove.visibility = View.GONE
                        Snackbar.make(binding.root, it.data.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Snackbar.make(binding.root, it.data.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }


        Log.d("DetailActivity Time: ", "execution time ${System.currentTimeMillis() - startTime} ms")
    }

    private fun setupPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, krsFragment)
        val sectionsPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                showApproveButton(
                    position == 2 &&
                            student.krsIsLocked == true &&
                            student.krsIsApproved == false
                )
            }
        }
        binding.detailViewPager.registerOnPageChangeCallback(sectionsPagerChangeCallback)
        binding.detailViewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.detailTabs, binding.detailViewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun showApproveButton(isShowButton: Boolean) {
        binding.fabApprove.visibility = if (isShowButton) View.VISIBLE else View.GONE
    }

    private fun showApproveAlertDialog(token: String, krsId: Int) {
        MyAlertDialog().setActionAlertDialog(
            this,
            "Apakah yakin untuk menyetujui KRS ini?",
            "Setujui"
        ) {
            viewModel.approveKrs(token, krsId)
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        val TAB_TITLES = arrayOf(
            "KHS",
            "TRANSKRIP",
            "KRS"
        )
        const val EXTRA_STUDENT = "extra_student"
        const val EXTRA_TOKEN = "extra_token"
        fun start(activity: Activity, student: Student, token: String) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(EXTRA_STUDENT, student)
            intent.putExtra(EXTRA_TOKEN, token)
            activity.startActivity(intent)
        }
    }
}