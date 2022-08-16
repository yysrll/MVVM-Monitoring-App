package com.yusril.mvvmmonitoring.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SubjectAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.FragmentGradeBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity.Companion.EXTRA_STUDENT
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity.Companion.TAB_TITLES
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GradeFragment : Fragment() {

    private lateinit var errorAlertDialog: AlertDialog
    private lateinit var binding: FragmentGradeBinding
    private lateinit var student: Student
    private lateinit var subjectAdapter: SubjectAdapter
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGradeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = requireArguments().getInt(ARG_SECTION_NUMBER, 0)
        student = activity?.intent?.getParcelableExtra(EXTRA_STUDENT)!!

        initRecyclerView()

        getListStudyGrade(index, student)
        lifecycleScope.launchWhenCreated {
            viewModel.listStudyGrade.collect { listStudyResult ->
                when (listStudyResult.status) {
                    Status.LOADING -> {
                        showLoading(true)
                        Log.d("$TAG ${TAB_TITLES[index-1]}", "Loading")
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        showEmptyView(false)
                        Log.d("$TAG ${TAB_TITLES[index-1]}", "Success: ${listStudyResult.data}")
                        listStudyResult.data?.let { data ->
                            subjectAdapter.addStudySubject(data)
                            val sks = data.sumOf { it.sks }
                            val scoreTotal = data.sumOf { it.score_total.toDouble() }
                            val gpa = (scoreTotal / sks)
                                .toBigDecimal()
                                .setScale(2, java.math.RoundingMode.HALF_UP)
                            val subject = data.size
                            binding.tvTotalSksValue.text = sks.toString()
                            binding.tvTotalSubjectValue.text = subject.toString()
                            binding.tvTotalGpaValue.text = gpa.toString()
                        }
                    }
                    Status.EMPTY -> {
                        showLoading(false)
                        showEmptyView(true)
                        Log.d("$TAG ${TAB_TITLES[index-1]}", "Empty")
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        errorAlertDialog.show()
                        Log.d("$TAG ${TAB_TITLES[index-1]}", "Error: ${listStudyResult.message}")
                    }
                }
            }
        }

        setErrorAlertDialog()
    }

    private fun showEmptyView(isEmpty: Boolean) {
        binding.emptyStatus.root.isInvisible = !isEmpty
    }

    private fun setErrorAlertDialog() {
        errorAlertDialog = MyAlertDialog().setErrorAlertDialog(
            requireActivity(),
            resources.getString(R.string.error_dialog_title),
            resources.getString(R.string.error_dialog_description),
            resources.getString(R.string.try_again)
        ) {
            viewModel.getStudentProfile(student.nim)
        }
    }

    private fun initRecyclerView() {
        subjectAdapter = SubjectAdapter()
        val rv = binding.rvSubjects
        rv.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = subjectAdapter
        }
    }

    private fun getListStudyGrade(index: Int, student: Student) {
        when(index) {
            1 -> viewModel.getListStudyGrade(student.nim, "20212")
            2 -> viewModel.getListStudyGrade(student.nim)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvSubjects.isInvisible = isLoading
        binding.progressBar.isInvisible = !isLoading
    }

    companion object {
        val TAG: String = GradeFragment::class.java.simpleName
        const val ARG_SECTION_NUMBER = "section_number"
    }
}