package com.yusril.mvvmmonitoring.core.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusril.mvvmmonitoring.R
import com.yusril.mvvmmonitoring.core.domain.model.Student
import com.yusril.mvvmmonitoring.databinding.ItemRowStudentBinding
import com.yusril.mvvmmonitoring.ui.detail.DetailActivity

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
            with(binding) {
//                rvRowPhoto.setImageResource(R.drawable.ic_generic_avatar)
                rvRowName.text = student.nim
                rvRowNim.text = student.gpa
                rvRowSks.text = "${student.sks} SKS"
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