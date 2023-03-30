package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.AttendTestActivity
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.SubjectAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentTestGameBinding
import com.rcappstudios.qualityeducation.model.Test


class TestGameFragment : Fragment(){

    private lateinit var binding: FragmentTestGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTestGameBinding.inflate(layoutInflater)
        return binding.root
    }


}