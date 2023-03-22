package com.rcappstudios.qualityeducation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint.Join
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rcappstudios.qualityeducation.adapters.RoomMatesAdapter
import com.rcappstudios.qualityeducation.databinding.ActivityStudentRoomBinding
import com.rcappstudios.qualityeducation.model.JoinRoomModel
import io.agora.rtc2.*

class StudentRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentRoomBinding
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    private val appId = "a9c7eed08a8a4e4bba648e6e6e879163"
    private val channelName = "1"
    private val token = "007eJxTYFilzW1b8Stc+C6TCn+vn4m21eaf21jYOY5+mZ59VGRSlYcCQ6JlsnlqaoqBRaJFokmqSVJSopmJRaoZEFqYWxqaGUsZiac0BDIymJ8uZGCEQhCfkcGQgQEA/+EaZg==="
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null

    // UI elements
//    private var infoText: TextView? = null
//    private var joinLeaveButton: Button? = null

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
//            runOnUiThread { infoText!!.text = "Remote user joined: $uid" }
        }
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
//            runOnUiThread { infoText!!.text = "Waiting for a remote user to join" }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
//            if (isJoined) runOnUiThread { infoText!!.text = "Waiting for a remote user to join" }
        }

        override fun onLeaveChannel(stats: RtcStats) {
//            runOnUiThread { infoText!!.text = "Press the button to join a channel" }
            isJoined = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        init()
    }

    private fun init(){
        fetchMatesDetails()
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        setupVoiceSDKEngine();
//        joinLeaveButton = findViewById(R.id.joinLeaveButton);
//        infoText = findViewById(R.id.infoText);
    }

    private fun setupVoiceSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            throw RuntimeException("Check the error.")
        }
    }

    private fun joinChannel() {
        val options = ChannelMediaOptions()
        options.autoSubscribeAudio = true
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
        agoraEngine!!.joinChannel(token, channelName, uid, options)
    }

    fun joinLeaveChannel(view: View?) {
        //TODO: Change th status of the mic button
        if (isJoined) {
            agoraEngine!!.leaveChannel()
//            joinLeaveButton!!.text = "Join"
        } else {
            joinChannel()
//            joinLeaveButton!!.text = "Leave"
        }
    }

    private fun fetchMatesDetails(){
        FirebaseDatabase.getInstance().getReference("Room/-NQtlKL0du600SeiAvE9/mates")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val matesList = arrayListOf<JoinRoomModel>()
                        for(c in snapshot.children){
                            matesList.add(c.getValue(JoinRoomModel::class.java)!!)
                        }
                        Log.d("TAGData", "onDataChange: ${matesList.size}")
                        initMatesRvAdapter(matesList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun initMatesRvAdapter(mates: ArrayList<JoinRoomModel>){
        binding.roomMates.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.roomMates.adapter = RoomMatesAdapter(this, mates)
    }

    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine!!.leaveChannel()
        Thread {
            RtcEngine.destroy()
            agoraEngine = null
        }.start()
    }

}