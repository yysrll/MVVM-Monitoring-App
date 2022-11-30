package com.yusril.mvvmmonitoring.ui.detail.transcript

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
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SubjectAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.FragmentTranskripBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity.Companion.EXTRA_STUDENT
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity.Companion.EXTRA_TOKEN
import com.yusril.mvvmmonitoring.ui.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranscriptFragment : Fragment() {

    private lateinit var errorAlertDialog: AlertDialog
    private lateinit var binding: FragmentTranskripBinding
    private lateinit var student: Student
    private lateinit var token: String
    private lateinit var subjectAdapter: SubjectAdapter
    private var startTime: Long = 0L
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startTime = System.currentTimeMillis()
        binding = FragmentTranskripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = requireArguments().getInt(ARG_SECTION_NUMBER, 0)
        student = activity?.intent?.getParcelableExtra(EXTRA_STUDENT)!!
        token = activity?.intent?.getStringExtra(EXTRA_TOKEN).toString()

        initRecyclerView()
        viewModel.getListStudyGrade(token, student.nim)

        lifecycleScope.launch {
            viewModel.listStudyGrade.collect { listStudyResult ->
                when (listStudyResult.status) {
                    Status.LOADING -> {
                        showLoading(true)
                        showEmptyView(false)
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        showEmptyView(false)
                        listStudyResult.data?.let { data ->
                            subjectAdapter.addStudySubject(data.subjects)
                            binding.tvTotalSksValue.text = data.totalSks.toString()
                            binding.tvTotalSubjectValue.text = data.totalSubject.toString()
                            binding.tvTotalGpaValue.text = data.totalGpa.toString()
                        }
                    }
                    Status.EMPTY -> {
                        showLoading(false)
                        showEmptyView(true)
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showEmptyView(false)
                        errorAlertDialog.show()
                    }
                }
            }
        }
        Log.d("Fragment $index Time: ", "execution time ${System.currentTimeMillis() - startTime} ms")
    }

    private fun showEmptyView(isEmpty: Boolean) {
        binding.includeEmptyStatus.root.isInvisible = !isEmpty
        binding.rvSubjects.isInvisible = isEmpty
        binding.tvTotalSksValue.text = "0"
        binding.tvTotalSubjectValue.text = "0"
        binding.tvTotalGpaValue.text = "0"
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

    private fun showLoading(isLoading: Boolean) {
        binding.rvSubjects.isInvisible = isLoading
        binding.progressBar.isInvisible = !isLoading
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}