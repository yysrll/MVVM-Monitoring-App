package com.yusril.mvvmmonitoring.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.databinding.FragmentGradeBinding

class GradeFragment : Fragment() {

    private lateinit var binding: FragmentGradeBinding

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

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        binding.tvGradeTitle.text = "Grade $index"
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}