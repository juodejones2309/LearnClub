package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemRoom2Binding
import com.rcappstudios.qualityeducation.databinding.ItemRoomBinding
import com.rcappstudios.qualityeducation.model.RoomModel

class PeerLearningAdapter(
    private val context: Context,
    private val roomList: MutableList<RoomModel>,
    private val isDetailFragment: Boolean,
    private val onClick: (Int, RoomModel) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var binding1:  ItemRoomBinding
    private lateinit var binding2: ItemRoom2Binding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(isDetailFragment){
            binding2 = ItemRoom2Binding.inflate(LayoutInflater.from(context),parent, false)
            ViewHolder(binding2.root)
        } else {
            binding1 = ItemRoomBinding.inflate(LayoutInflater.from(context),parent, false)
            ViewHolder(binding1.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val room = roomList[position]
        if(isDetailFragment){
            binding2.roomTitle.text = room.topicName
            binding2.roomOwner.text = room.creatorName
            binding2.subject.text = room.subject

            binding2.btnJoinRoom.setOnClickListener {
                onClick.invoke(position, room)
            }
        } else {
            binding1.roomTitle.text = room.topicName
            binding1.roomOwner.text = room.creatorName
            binding1.subject.text = room.subject

            binding1.btnJoinRoom.setOnClickListener {
                onClick.invoke(position, room)
            }
        }

    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}