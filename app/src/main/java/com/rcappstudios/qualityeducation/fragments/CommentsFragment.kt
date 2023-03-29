package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentCommentsBinding


class CommentsFragment : Fragment() {

    private val args: CommentsFragmentArgs by navArgs()
    private lateinit var binding: FragmentCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentsBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAGData", "onViewCreated: ${args.questionID}")

    }

}