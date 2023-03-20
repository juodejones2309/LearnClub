package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.StudentRoomActivity
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
        // Inflate the layout for this fragment
        binding = FragmentPeerLearningBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = FirebaseDatabase.getInstance().reference.push().key
        /*FirebaseDatabase.getInstance().getReference("Room/$key")
            .setValue(
                RoomModel(
                    hostID = FirebaseAuth.getInstance().uid,
                    topicName = "Social Environment",
                    timeStamp = Calendar.getInstance().timeInMillis,
                    creatorName = "Hari Haran R C",
                    roomID = key
                )
            )*/
        clickListener()
    }

    private fun clickListener(){
        binding.joinRoom.setOnClickListener {
            initJoinRoom()
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