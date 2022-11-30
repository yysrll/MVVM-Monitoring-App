package com.yusril.mvvmmonitoring.ui.detail.krs

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
import com.yusril.mvvmmonitoring.databinding.FragmentKrsBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity
import com.yusril.mvvmmonitoring.ui.detail.DetailViewModel
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class KrsFragment : Fragment() {

    private lateinit var binding: FragmentKrsBinding
    private lateinit var errorAlertDialog: AlertDialog
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
        binding = FragmentKrsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = requireArguments().getInt(ARG_SECTION_NUMBER, 0)
        student = activity?.intent?.getParcelableExtra(DetailActivity.EXTRA_STUDENT)!!
        token = activity?.intent?.getStringExtra(DetailActivity.EXTRA_TOKEN).toString()

        initRecyclerView()
        setErrorAlertDialog(token, student.id)
        viewModel.getKrs(token, student.id)

        lifecycleScope.launch {
            viewModel.listKrs.collect { listStudyResult ->
                when (listStudyResult.status) {
                    Status.LOADING -> {
                        showLoading(true)
                        showEmptyView(false)
                    }
                    Status.SUCCESS -> {
                        showLoading(false)
                        showEmptyView(false)
                        listStudyResult.data?.let { data ->
                            data.listKrs?.let {
                                subjectAdapter.addStudySubject(it)
                                val sks = it.sumOf { matkul ->
                                    matkul.sks
                                }
                                val subject = it.size
                                binding.tvTotalSksValue.text = sks.toString()
                                binding.tvTotalSubjectValue.text = subject.toString()
                                binding.tvLockedStatusValue.text = if (data.isLocked) "Dikunci" else "Belum Dikunci"
                                binding.tvApprovedStatusValue.text = if (data.isApproved) "Disetujui" else "Belum Disetujui"
                            }
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
        binding.emptyStatus.root.isInvisible = !isEmpty
        binding.rvSubjects.isInvisible = isEmpty
        binding.tvTotalSksValue.text = "0"
        binding.tvTotalSubjectValue.text = "0"
    }

    private fun initRecyclerView() {
        subjectAdapter = SubjectAdapter(isKrs = true)
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

    private fun setErrorAlertDialog(token: String, studentId: Int) {
        errorAlertDialog = MyAlertDialog().setErrorAlertDialog(
            requireActivity(),
            resources.getString(R.string.error_dialog_title),
            resources.getString(R.string.error_dialog_description),
            resources.getString(R.string.try_again)
        ) {
            viewModel.getKrs(token, studentId)
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }

    fun onKrsApproved() {
        viewModel.getKrs(token, student.id)
    }
}