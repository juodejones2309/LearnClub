package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemAskDoubtBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.squareup.picasso.Picasso

class AskQuestionsAdapter(
    private val context: Context,
    private val questionsList: ArrayList<QuestionModel>,
    private val onClick: (Int ,String) ->Unit
): RecyclerView.Adapter<AskQuestionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemAskDoubtBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        binding = ItemAskDoubtBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AskQuestionsAdapter.ViewHolder, position: Int) {
        val question = questionsList[position]
        if(question.imageUrl != null){
            binding.ivQuestion.visibility = View.VISIBLE
            Picasso.get()
                .load(question.imageUrl)
                .fit().centerCrop()
                .into(binding.ivQuestion)
        } else {
            binding.ivQuestion.visibility = View.GONE
        }

        binding.tvQuestion.text = question.question

        if(question.userImageUrl != null){
            Picasso.get().load(question.userImageUrl)
                .fit().centerCrop().into(binding.profileImage)
        }

        if(question.userName != null){
            binding.tvName.text = question.userName
        }

        binding.comments.setOnClickListener {
            onClick.invoke(2, question.questionID!!)
        }
        binding.youtube.setOnClickListener {
            if(question.question != null)
                onClick.invoke(1, question.question.toString())
        }
    }



    override fun getItemCount(): Int = questionsList.size

}