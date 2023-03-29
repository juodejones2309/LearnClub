package com.rcappstudios.qualityeducation.fragments.whiteboard

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.StudentRoomActivity
import com.rcappstudios.qualityeducation.databinding.FragmentRoomWhiteBoardBinding

class RoomWhiteBoardFragment : Fragment() {

    private lateinit var binding: FragmentRoomWhiteBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomWhiteBoardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRoomData()
        Log.d("TAGData2", "onViewCreated: ${StudentRoomActivity.roomID}")
    }

    private fun initWhiteBoard(hostID: String){
        binding.paintView.initRoom(StudentRoomActivity.roomID, StudentRoomActivity.subject ,requireActivity())
        Log.d("TAGData", "initWhiteBoard: $hostID")
        if(hostID == FirebaseAuth.getInstance().uid){
            binding.paintView.myTurn = true
        }
        binding.paintView.pathUpdateListener()
    }

    private fun getRoomData(){
        FirebaseDatabase.getInstance().getReference("Room/${StudentRoomActivity.subject}/${StudentRoomActivity.roomID}/hostID")
            .get().addOnSuccessListener {
                if(it.exists()){
                    initWhiteBoard(it.value as String)
                }
            }
    }

}