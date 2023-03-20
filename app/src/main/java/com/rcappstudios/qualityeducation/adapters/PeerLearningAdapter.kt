package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemRoomBinding
import com.rcappstudios.qualityeducation.model.RoomModel

class PeerLearningAdapter(
    private val context: Context,
    private val roomList: MutableList<RoomModel>,
    private val onClick: (Int, RoomModel) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemRoomBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       binding = ItemRoomBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val room = roomList[position]
        binding.roomTitle.text = room.topicName
        binding.roomOwner.text = room.creatorName
        binding.subject.text = room.subject

        binding.btnJoinRoom.setOnClickListener {
            onClick.invoke(position, room)
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}