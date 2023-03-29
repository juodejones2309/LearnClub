package com.rcappstudios.qualityeducation.fragments.mentors;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.rcappstudios.qualityeducation.R;
import com.rcappstudios.qualityeducation.databinding.FragmentMentorVideoBinding;
import com.rcappstudios.qualityeducation.databinding.FragmentVideoBinding;

import io.agora.rtc2.RtcEngine;

public class MentorVideoFragment extends Fragment{

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS =
            {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };

    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "a9c7eed08a8a4e4bba648e6e6e879163";
    // Fill the channel name.
    private String channelName = "2";
    // Fill the temp token generated on Agora Console.
    private String token = "007eJxTYIiq/T7Pkbs28ubPS3Miv6w4eTP6gMjlv8887/Rt+1qr+0NRgSHRMtk8NTXFwCLRItEk1SQpKdHMxCLVDAgtzC0NzYw7WeRTGgIZGUS2HGVhZIBAEJ+RwYiBAQAZyyFd";
    // An integer that identifies the local user.
    private int uid = 0;
    private boolean isJoined = false;

    private RtcEngine agoraEngine;
    //SurfaceView to render local video in a Container.
    private SurfaceView localSurfaceView;
    //SurfaceView to render Remote video in a Container.
    private SurfaceView remoteSurfaceView;
    // A toggle switch to change the User role.
    private Switch audienceRole;

    private FragmentMentorVideoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMentorVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }



}