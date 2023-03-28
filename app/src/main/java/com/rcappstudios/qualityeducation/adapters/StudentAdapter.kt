package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemStudentBinding
import com.rcappstudios.qualityeducation.model.InitStudentMessage
import com.squareup.picasso.Picasso

class StudentAdapter(
    private val context: Context,
    private var studentList: MutableList<InitStudentMessage>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var  binding: ItemStudentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemStudentBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val student = studentList[position]

        Picasso.get()
            .load("https://picsum.photos/200/300")
            .fit().centerCrop()
            .into(binding.ivStudent)

        binding.studentName.text = student.studentName

        binding.root.setOnClickListener {
            onClick.invoke(student.studentUserId.toString())
        }
    }

     fun updateStudent(student: InitStudentMessage){
        studentList.add(student)
        notifyDataSetChanged()
    }




}