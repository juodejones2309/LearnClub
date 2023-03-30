package com.rcappstudios.qualityeducation

import android.Manifest

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
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
    private val token = "007eJxTYNj0muOd6ZyKHYpZXMl3/snNlyuavn9pYHHitufhWx+vjjdXYEi0TDZPTU0xsEi0SDRJNUlKSjQzsUg1A0ILc0tDM2NeQ9WUhkBGhtO3DBgYoRDEZ2QwZGAAAD0zHow="
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null

    companion object{
        @JvmStatic
        var roomID : String = ""
        @JvmStatic
        var hostID: String = ""
        @JvmStatic
        var subject: String = ""
    }


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
        setUpBottomNavigation()
        roomID = intent.getStringExtra("RoomId")!!
        hostID = intent.getStringExtra("hostID")!!
        subject = intent.getStringExtra("Subject")!!
        init()
        Log.d("RoomID", "onCreate:activity $roomID")
        binding.bottomBar.setItemSelected(R.id.roomWhiteBoard)

    }
    private fun init(){
        fetchMatesDetails()
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        setupVoiceSDKEngine();
        joinChannel()
        binding.audioSwitch.isChecked = true

        binding.audioSwitch.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1){
                    agoraEngine!!.enableAudio()
                    Toast.makeText(applicationContext, "Audio un muted", Toast.LENGTH_LONG).show()
                } else{
                    agoraEngine!!.disableAudio()
                    Toast.makeText(applicationContext, "Audio muted", Toast.LENGTH_LONG).show()

                }
            }

        })
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

    private fun setUpBottomNavigation(){
        binding.bottomBar.setOnItemSelectedListener { item ->
            when(item){
                R.id.roomWhiteBoard->{
                    switchToFragment(R.id.roomWhiteBoardFragment2)
                }
                R.id.roomVideoStream->{
                    agoraEngine!!.leaveChannel()
                    switchToFragment(R.id.videoFragment)
                }
                R.id.roomChat->{
                    switchToFragment(R.id.roomChatFragment2)
                }
            }
        }
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
        //TODO: Change the room id
        FirebaseDatabase.getInstance().getReference("Room/${subject}/${roomID}/mates")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val matesList = arrayListOf<JoinRoomModel>()
                        for(c in snapshot.children){
                            matesList.add(c.getValue(JoinRoomModel::class.java)!!)
                        }
//                        Log.d("TAGData", "onDataChange: ${matesList.size}")
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

        FirebaseDatabase.getInstance().getReference("Room/$subject/${roomID}/mates/${ FirebaseAuth.getInstance().uid}")
            .removeValue()
    }

    private fun getNavController(): NavController {
        return (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment).navController
    }

    private fun switchToFragment(destinationId: Int) {
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(destinationId)
        }
    }

    private fun isFragmentInBackStack(destinationId: Int) =
        try {
            getNavController().getBackStackEntry(destinationId)
            true
        } catch (e: Exception) {
            false
        }

    override fun onBackPressed() {
        if(binding.bottomBar.getSelectedItemId() == R.id.roomWhiteBoard){

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            builder.setTitle("Leave Room")
            builder.setMessage("Are you sure to leave room?")
            builder .setPositiveButton("Ok"){_,_->
                super.onBackPressed()
                }.setNegativeButton("Cancel", /* listener = */ null)
                .show();
        }

        binding.bottomBar.setItemSelected(R.id.roomWhiteBoard)
    }


}