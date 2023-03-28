package com.rcappstudios.qualityeducation.fragments.mentors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.StudentAdapter
import com.rcappstudios.qualityeducation.databinding.ActivityMentorViewMessagesBinding
import com.rcappstudios.qualityeducation.model.InitStudentMessage
import com.rcappstudios.qualityeducation.model.MessageModel
import com.rcappstudios.qualityeducation.utils.Constants

class MentorViewMessagesActivity : AppCompatActivity() {

    private lateinit var subject: String
    private lateinit var binding: ActivityMentorViewMessagesBinding
    private lateinit var adapter: StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentorViewMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subject = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getString("subject", "Physics")!!
        initRv()
        getStudentDetails()

    }

    private fun getStudentDetails(){
        FirebaseDatabase.getInstance()
            .getReference("Mentors/$subject/BPbcbSibAHejJmVf8M26z0ZPCfb2/connections")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    for(c in it.children){
                        val student = c.getValue(InitStudentMessage::class.java)
                        adapter.updateStudent(student!!)
                    }
                }
            }
    }

    private fun initRv(){
        binding.rvStudents.layoutManager = LinearLayoutManager(this)

        adapter = StudentAdapter(this, mutableListOf()){ studentId->
            val intent = Intent(this, MentorSupportActivity::class.java)
            intent.putExtra("studentUserId", studentId)
            startActivity(intent)
        }

        binding.rvStudents.adapter = adapter
    }




}