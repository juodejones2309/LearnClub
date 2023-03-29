package com.rcappstudios.qualityeducation.fragments.mentors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentMentorWhiteBoardBinding

class MentorWhiteBoardFragment : Fragment() {

    private lateinit var binding: FragmentMentorWhiteBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMentorWhiteBoardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.paintView.myTurn = MentorSupportActivity.isMentor
        initWhiteBoard()
    }

    private fun initWhiteBoard(){
        binding.paintView.initMentor(MentorSupportActivity.subject, MentorSupportActivity.mentorUserId, MentorSupportActivity.studentUserId, requireActivity())
        binding.paintView.pathUpdateListener()
    }

}