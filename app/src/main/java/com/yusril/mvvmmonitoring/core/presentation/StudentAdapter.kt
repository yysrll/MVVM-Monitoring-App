package com.yusril.mvvmmonitoring.core.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.databinding.ItemRowStudentBinding
import java.util.Calendar

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.RecyclerViewHolder>() {
    private val listStudent = ArrayList<Student>()
    private var onItemClickCallback: OnItemClickCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addStudents(items: List<Student>) {
        listStudent.clear()
        listStudent.addAll(items)
        this.notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class RecyclerViewHolder(private val binding: ItemRowStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            val isStatusVisible = student.krsIsLocked == false || student.krsIsApproved == false
            binding.rvRowKrsStatus.isVisible = isStatusVisible

            binding.rvRowKrsStatus.apply {
                if (student.krsIsLocked == false) {
                    text = "KRS belum dikunci"
                } else if(student.krsIsApproved == false) {
                    text = "KRS belum disetujui"
                }
            }

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)

            val diffYear = year - student.year!!.toInt()
            binding.rvRowStatus.apply {
                if (diffYear == 2 && student.sks < 48) text = "Evaluasi 4 Semester"
                else if (diffYear == 7) text = "Evaluasi 14 Semester"
                else isGone = true
            }


            with(binding) {
                rvRowName.text = student.name
                rvRowNim.text = student.nim
                rvRowYear.text = student.year
                rvRowIpkSks.text = this@RecyclerViewHolder.itemView.context.getString(R.string.total_ipk_sks, student.gpa, student.sks)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRowStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listStudent[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listStudent[position])
        }
    }

    override fun getItemCount(): Int = listStudent.size

    interface OnItemClickCallback {
        fun onItemClicked(student: Student)
    }
}