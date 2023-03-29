package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemAskDoubt2Binding
import com.rcappstudios.qualityeducation.databinding.ItemAskDoubtBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.squareup.picasso.Picasso

class AskQuestionsAdapter(
    private val context: Context,
    private val questionsList: ArrayList<QuestionModel>,
    private val isDetailFragment: Boolean,
    private val onClick: (Int ,String) ->Unit
): RecyclerView.Adapter<AskQuestionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view)

    private lateinit var binding1: ItemAskDoubtBinding
    private lateinit var binding2: ItemAskDoubt2Binding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        return if(isDetailFragment){
            binding2 = ItemAskDoubt2Binding.inflate(LayoutInflater.from(context),parent,false)
            ViewHolder(binding2.root)
        } else {
            binding1 = ItemAskDoubtBinding.inflate(LayoutInflater.from(context),parent,false)
            ViewHolder(binding1.root)
        }
    }

    override fun onBindViewHolder(holder: AskQuestionsAdapter.ViewHolder, position: Int) {
        val question = questionsList[position]
        if(isDetailFragment){
            if(question.imageUrl != null){
                binding2.ivQuestion.visibility = View.VISIBLE
                Picasso.get()
                    .load(question.imageUrl)
                    .fit().centerCrop()
                    .into(binding2.ivQuestion)
            } else {
                binding2.ivQuestion.visibility = View.GONE
            }

            binding2.tvQuestion.text = question.question

            if(question.userImageUrl != null){
                Picasso.get().load(question.userImageUrl)
                    .fit().centerCrop().into(binding2.profileImage)
            }

            if(question.userName != null){
                binding2.tvName.text = question.userName
            }

            binding2.comments.setOnClickListener {
                onClick.invoke(2, question.questionID!!)
            }
            binding2.youtube.setOnClickListener {
                if(question.question != null)
                    onClick.invoke(1, question.question.toString())
            }
        } else{
            if(question.imageUrl != null){
                binding1.ivQuestion.visibility = View.VISIBLE
                Picasso.get()
                    .load(question.imageUrl)
                    .fit().centerCrop()
                    .into(binding1.ivQuestion)
            } else {
                binding1.ivQuestion.visibility = View.GONE
            }

            binding1.tvQuestion.text = question.question

            if(question.userImageUrl != null){
                Picasso.get().load(question.userImageUrl)
                    .fit().centerCrop().into(binding1.profileImage)
            }

            if(question.userName != null){
                binding1.tvName.text = question.userName
            }

            binding1.comments.setOnClickListener {
                onClick.invoke(2, question.questionID!!)
            }
            binding1.youtube.setOnClickListener {
                if(question.question != null)
                    onClick.invoke(1, question.question.toString())
            }
        }

    }



    override fun getItemCount(): Int = questionsList.size

}