package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.StudentRoomActivity
import com.rcappstudios.qualityeducation.adapters.PeerLearningAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentPeerLearningBinding
import com.rcappstudios.qualityeducation.model.JoinRoomModel
import com.rcappstudios.qualityeducation.model.RoomModel
import java.util.Calendar


class PeerLearningFragment : Fragment() {

    private lateinit var binding: FragmentPeerLearningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeerLearningBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRoomList()
        clickListener()
    }

    private fun clickListener(){
    }
    private fun getRoomList(){
        FirebaseDatabase.getInstance().getReference("Room")
            .get().addOnSuccessListener {
                if(it.exists()){
                    val roomList = mutableListOf<RoomModel>()
                    for(c in it.children){
                        roomList.add(c.getValue(RoomModel::class.java)!!)
                    }
                    initRv(roomList)
                }
            }
    }
    private fun initRv(roomList: MutableList<RoomModel>){
        binding.rvPeer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPeer.adapter =
            PeerLearningAdapter(requireContext(), roomList){int, room->

            }
    }

    private fun initJoinRoom(){
        // For Testing
        FirebaseDatabase.getInstance().getReference("Room/-NQtlKL0du600SeiAvE9/mates/${FirebaseAuth.getInstance().uid}")
            .setValue(
                JoinRoomModel(
                    userName = "Kumar",
                    userID = FirebaseAuth.getInstance().uid,
                    userImage = "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"
                )
            ).addOnSuccessListener {
                startActivity(Intent(requireContext(), StudentRoomActivity::class.java))
            }
    }


}