package com.rcappstudios.qualityeducation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.rcappstudios.qualityeducation.StudentRoomActivity
import com.rcappstudios.qualityeducation.databinding.FragmentRoomVideoStreamBinding

import io.agora.base.internal.ContextUtils.getApplicationContext
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine


class RoomVideoStreamFragment : Fragment() {

    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    // Fill the App ID of your project generated on Agora Console.
    private val appId = "a9c7eed08a8a4e4bba648e6e6e879163"

    // Fill the channel name.
    private val channelName = "1"

    // Fill the temp token generated on Agora Console.
    private val token = "007eJxTYFi98MDyuG3iP25oa/F+7VQ1/JiYWxm2bF7v8e2+PTrHfJMUGBItk81TU1MMLBItEk1STZKSEs1MLFLNgNDC3NLQzPjLEamUhkBGhr/2k5kYGSAQxGdkMGRgAABTDR+S"

    // An integer that identifies the local user.
    private val uid = 0
    private var isJoined = false

    private var agoraEngine: RtcEngine? = null

    //SurfaceView to render local video in a Container.
    private var localSurfaceView: SurfaceView? = null

    //SurfaceView to render Remote video in a Container.
    private var remoteSurfaceView: SurfaceView? = null

    // A toggle switch to change the User role.
//    private var audienceRole: Switch? = null
    private var audienceRole = false

    private lateinit var binding: FragmentRoomVideoStreamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomVideoStreamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}