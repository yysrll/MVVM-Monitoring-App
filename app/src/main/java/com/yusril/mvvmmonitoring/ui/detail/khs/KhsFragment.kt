package com.yusril.mvvmmonitoring.ui.detail.khs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Semester
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.core.presentation.SubjectAdapter
import com.yusril.mvvmmonitoring.core.vo.Status
import com.yusril.mvvmmonitoring.databinding.FragmentKhsBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity
import com.yusril.mvvmmonitoring.ui.detail.DetailViewModel
import com.yusril.mvvmmonitoring.ui.utils.MyAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class KhsFragment : Fragment() {

    private lateinit var binding: FragmentKhsBinding
    private lateinit var errorAlertDialog: AlertDialog
    private lateinit var student: Student
    private lateinit var token: String
    private lateinit var subjectAdapter: SubjectAdapter
    private lateinit var semesterCode: String
    private var startTime: Long = 0L
    private var semesterItems: List<Semester> = listOf()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startTime = System.currentTimeMillis()
        binding = FragmentKhsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = requireArguments().getInt(ARG_SECTION_NUMBER, 0)
        student = activity?.intent?.getParcelableExtra(DetailActivity.EXTRA_STUDENT)!!
        token = activity?.intent?.getStringExtra(DetailActivity.EXTRA_TOKEN).toString()

        initRecyclerView()
        setErrorAlertDialog()
        setSemester()

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

        lifecycleScope.launch {
                viewModel.listSemester.collect { listSemesterResult ->
                    when (listSemesterResult.status) {
                        Status.SUCCESS -> {
                            listSemesterResult.data?.let { data ->
                                semesterItems = data
                                val semesterString = data.map {
                                    "Semester " + it.jenis + " " + it.tahunAjaran
                                }
                                val myAdapter = ArrayAdapter(
                                    requireContext(),
                                    R.layout.semester_list_item,
                                    semesterString
                                )
                                binding.menuSemester.apply {
                                    setAdapter(myAdapter)
                                    setText(semesterString[0], false)
                                }
                                semesterCode = data[0].kode
                                getListStudyGrade(student, semesterCode)
                            }
                        }
                        Status.EMPTY -> {
                        }
                        else -> {
                            if (listSemesterResult.status == Status.ERROR) errorAlertDialog.show()
                        }
                    }
                }
            }
        Log.d("Fragment $index Time: ", "execution time ${System.currentTimeMillis() - startTime} ms")
    }

    private fun setSemester() {
        viewModel.getSemester(token)

        binding.menuSemester.apply {
            setOnItemClickListener { _, _, position, _ ->
                semesterCode = semesterItems[position].kode
                getListStudyGrade(student, semesterCode)
            }
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        binding.emptyStatus.root.isInvisible = !isEmpty
        binding.rvSubjects.isInvisible = isEmpty
        binding.tvTotalSksValue.text = "0"
        binding.tvTotalSubjectValue.text = "0"
        binding.tvTotalGpaValue.text = "0"
    }

    private fun setErrorAlertDialog() {
        errorAlertDialog = MyAlertDialog().setErrorAlertDialog(
            requireActivity(),
            resources.getString(R.string.error_dialog_title),
            resources.getString(R.string.error_dialog_description),
            resources.getString(R.string.try_again)
        ) {
            setSemester()
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

    private fun getListStudyGrade(student: Student, code: String) {
        viewModel.getListStudyGrade(token, student.nim, code)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvSubjects.isInvisible = isLoading
        binding.progressBar.isInvisible = !isLoading
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }


}