package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemMatesBinding
import com.rcappstudios.qualityeducation.databinding.ItemProblemStatementBinding
import com.rcappstudios.qualityeducation.model.JoinRoomModel
import com.rcappstudios.qualityeducation.model.ProblemStatement
import com.squareup.picasso.Picasso

class ProblemAdapter(
    private val context: Context,
    private val problemList: MutableList<ProblemStatement>,
    private val onClick: (String) ->Unit
): RecyclerView.Adapter<ProblemAdapter.ViewHolder>(){

    private lateinit var binding: ItemProblemStatementBinding
    class ViewHolder(view: View) :RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        binding = ItemProblemStatementBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return problemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val problem = problemList[position]
        binding.title.text = problem.title
        binding.tvDescription.text = problem.description

        Picasso
            .get()
            .load(problem.imageUrl)
            .fit()
            .centerCrop()
            .into(binding.ivProblemStatement)
        binding.root.setOnClickListener{
            onClick.invoke(problem.link!!)
        }
    }


}