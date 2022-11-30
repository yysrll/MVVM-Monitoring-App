package com.yusril.mvvmmonitoring.core.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yusril.mvvmmonitoring.ui.detail.khs.KhsFragment
import com.yusril.mvvmmonitoring.ui.detail.transcript.TranscriptFragment
import com.yusril.mvvmmonitoring.ui.detail.transcript.TranscriptFragment.Companion.ARG_SECTION_NUMBER
import com.yusril.mvvmmonitoring.ui.detail.krs.KrsFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val krsFragment: KrsFragment,
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment = KhsFragment()
            1 -> fragment = TranscriptFragment()
            2 -> fragment = krsFragment
        }
        fragment?.arguments = Bundle().apply {
            putInt(ARG_SECTION_NUMBER, position + 1)
        }
        return fragment as Fragment
    }

}