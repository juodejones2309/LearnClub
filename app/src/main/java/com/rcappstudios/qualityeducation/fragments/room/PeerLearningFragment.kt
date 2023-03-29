package com.rcappstudios.qualityeducation.fragments.room

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.StudentRoomActivity
import com.rcappstudios.qualityeducation.adapters.PeerLearningAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentPeerLearningBinding
import com.rcappstudios.qualityeducation.fragments.SubjectDetailFragmentArgs
import com.rcappstudios.qualityeducation.model.JoinRoomModel
import com.rcappstudios.qualityeducation.model.RoomModel
import com.rcappstudios.qualityeducation.utils.Constants
import java.util.Calendar


class PeerLearningFragment : Fragment() {

    private lateinit var binding: FragmentPeerLearningBinding
    val navArgs: PeerLearningFragmentArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeerLearningBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = requireActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getString(Constants.NAME, null)!!
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        getRoomList()
        clickListener()
        Log.d("ArgsCheck", "onViewCreated: peerLearning ${navArgs.subject}")
    }

    private fun clickListener(){
        binding.fabCreateRoom.setOnClickListener {
            initBottomSheet()
        }
        binding.bottomSheet.btnCreateRoom.setOnClickListener {
            extractDetails()
        }
    }

    private fun initBottomSheet(){
        binding.bottomSheet.textRoomTitle.setText("Enter Room title")
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        val state = if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            BottomSheetBehavior.STATE_COLLAPSED
        } else {
            //TODO : Setup api call and recycler view adapter
            BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior.state = state
    }

    private fun extractDetails(){
        if(binding.bottomSheet.roomTitle.text.toString() != ""){
            createRoom()
        } else {
            Toast.makeText(requireContext(), "Please fill the details", Toast.LENGTH_LONG).show()
        }
    }

    private fun createRoom(){
        val pushKey = FirebaseDatabase.getInstance().getReference("Room/${navArgs.subject}").push().key
        FirebaseDatabase.getInstance().getReference("Room/${navArgs.subject}/$pushKey")
            .setValue(
                RoomModel(
                    hostID = FirebaseAuth.getInstance().uid,
                    topicName = binding.bottomSheet.roomTitle.text.toString(),
                    timeStamp = Calendar.getInstance().timeInMillis,
                    creatorName = userName,
                    roomID = pushKey,
                    subject = navArgs.subject
                )
            ) .addOnSuccessListener {
                initJoinRoom(pushKey.toString(), FirebaseAuth.getInstance().uid.toString())
            }
    }



    private fun getRoomList(){
        FirebaseDatabase.getInstance().getReference("Room/${navArgs.subject}")
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
                //TODO: Confirmation dialog should be appeared
                initJoinRoom(room.roomID.toString(), room.hostID.toString())
            }
    }

    private fun navigateToStudentRoomActivity(roomId: String, hostID: String){
        //TODO: Add correct subjects
        val intent = Intent(requireContext(), StudentRoomActivity::class.java)
        intent.putExtra("Subject", navArgs.subject)
        intent.putExtra("hostID",hostID )
        intent.putExtra("RoomId", roomId)
        startActivity(intent)
    }

    private fun initJoinRoom(roomId: String, hostID: String){
        // For Testing
        FirebaseDatabase.getInstance().getReference("Room/${navArgs.subject}/${roomId}/mates/${FirebaseAuth.getInstance().uid}")
            .setValue(
                JoinRoomModel(
                    userName = requireActivity()
                        .getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                        .getString(Constants.NAME, null),
                    userID = FirebaseAuth.getInstance().uid,
                    userImage = "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"
                )
            ).addOnSuccessListener {
                navigateToStudentRoomActivity(roomId, hostID)
            }
    }


}