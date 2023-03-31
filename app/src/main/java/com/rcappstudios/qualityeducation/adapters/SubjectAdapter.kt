package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.ItemSubjectBinding

class SubjectAdapter(
    private val context: Context,
    private var subjectList: MutableList<String>? = null,
    private var studentScore: Map<String, Int>? = null,
    private val onClick: SubjectClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemSubjectBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemSubjectBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = if (subjectList != null) {
        subjectList?.size!!
    } else {
        0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (subjectList != null) {
            binding.subjectTv.text = subjectList!![position]
            binding.subjectTv.setOnClickListener {
                onClick?.onSubjectClick(subjectList!![position])
            }
        } else {
            Log.d("TAGData", "onBindViewHolder: $studentScore")
            val studentName = studentScore!!.keys.elementAt(position)
            val score = studentScore!![studentName]
            binding.subjectTv.text = studentName
            if (score!! < 40) {
                binding.scoreTv.setTextColor(context.getColor(R.color.red))
            }
            binding.scoreTv.visibility = View.VISIBLE
            binding.scoreTv.text = score.toString()
        }
    }

    fun updateSubject(subject: String) {
        subjectList?.add(subject)
        notifyDataSetChanged()
    }

    interface SubjectClickListener {
        fun onSubjectClick(subject: String)
    }
}