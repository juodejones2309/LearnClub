package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemMatesBinding
import com.rcappstudios.qualityeducation.model.JoinRoomModel
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.squareup.picasso.Picasso

class RoomMatesAdapter(
    private val context: Context,
    private val matesList: ArrayList<JoinRoomModel>,
): RecyclerView.Adapter<RoomMatesAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view)
    private lateinit var binding: ItemMatesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMatesBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
       return matesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mate = matesList[position]

        if(mate.userImage != null){
            Picasso.get()
                .load(mate.userImage)
                .fit().centerCrop()
                .into(binding.profileImage)
        }

        binding.profileName.text = mate.userName
    }

}