package com.rcappstudios.qualityeducation.fragments.mentors

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.StudentRoomActivity
import com.rcappstudios.qualityeducation.adapters.ChatAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentMentorChatBinding
import com.rcappstudios.qualityeducation.model.InitStudentMessage
import com.rcappstudios.qualityeducation.model.MessageModel
import com.rcappstudios.qualityeducation.utils.Constants
import java.util.*

class MentorChatFragment : Fragment() {

    private var userName = ""
    private lateinit var binding: FragmentMentorChatBinding
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var chatList : MutableList<MessageModel>
    private lateinit var studentUid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        MentorSupportActivity.mentorUserId
        binding = FragmentMentorChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = requireActivity()
            .getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
            .getString(Constants.NAME,null)!!
        init()
        clickListener()

        if(MentorSupportActivity.isMentor){
            studentUid = MentorSupportActivity.studentUserId
        } else {
            studentUid = FirebaseAuth.getInstance().uid.toString()
            initStudentMessage()
        }

        readMessage()

    }

    private fun initStudentMessage(){
        FirebaseDatabase.getInstance().getReference("Mentors/${MentorSupportActivity.subject}/${MentorSupportActivity.mentorUserId}/connections/$studentUid")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    //DO Nothing
                } else {
                    FirebaseDatabase.getInstance().getReference("Mentors/${MentorSupportActivity.subject}/${MentorSupportActivity.mentorUserId}/connections/$studentUid")
                        .setValue(InitStudentMessage(userName, studentUid))
                }
            }
    }

    private fun clickListener(){
        binding.sendMessage.setOnClickListener {
            if(binding.messageBox.text.isNullOrEmpty()){
                //Do nothing
            } else {
                sendMessage(binding.messageBox.text.toString())
                binding.messageBox.text = null
            }
        }
    }

    private fun init(){
        chatList = mutableListOf()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext() )
        binding.chatRecyclerView.setHasFixedSize(true)
        chatAdapter = ChatAdapter(requireContext() , chatList)
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun sendMessage(message : String){

        val messageModel = MessageModel(message, FirebaseAuth.getInstance().uid ,userName , Date().time)

        FirebaseDatabase.getInstance()
            .getReference("Mentors/${MentorSupportActivity.subject}/${MentorSupportActivity.mentorUserId}/connections/$studentUid/chats").push()
            .setValue(messageModel)

    }
    private fun readMessage() {
        FirebaseDatabase.getInstance()
            .getReference("Mentors/${MentorSupportActivity.subject}/${MentorSupportActivity.mentorUserId}/connections/$studentUid/chats").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        chatList.clear()
                        for (dataSnapShot in snapshot.children) {
                            val message = dataSnapShot.getValue(MessageModel::class.java)
                            chatList.add(message!!)
                        }
                        chatAdapter.updateList(chatList)
                        binding.chatRecyclerView.scrollToPosition(chatList.size - 1)
                    }
                }
            })

    }






}