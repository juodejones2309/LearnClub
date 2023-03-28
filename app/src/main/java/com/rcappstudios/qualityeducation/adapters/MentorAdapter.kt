package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemMentorBinding
import com.rcappstudios.qualityeducation.model.mentors.MentorData
import com.squareup.picasso.Picasso

class MentorAdapter(
    private val context: Context,
    private val mentorList: MutableList<MentorData>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var  binding: ItemMentorBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemMentorBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = mentorList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mentor = mentorList[position]

        Picasso.get()
            .load("https://picsum.photos/200/300")
            .fit().centerCrop()
            .into(binding.ivMentor)

        binding.mentorName.text = mentor.mentorName
        binding.ratingBar.rating = 3.4F

        binding.root.setOnClickListener {
            onClick.invoke(mentor.userId.toString() )
        }
    }


}