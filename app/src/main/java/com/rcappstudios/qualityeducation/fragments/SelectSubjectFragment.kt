package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentSelectSubjectBinding
import com.rcappstudios.qualityeducation.chatgpt.ui.ChatGptActivity

class SelectSubjectFragment : Fragment() {

    private lateinit var binding: FragmentSelectSubjectBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
    }

    private fun clickListener(){
        binding.subject.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Physics"),
                R.id.subjectDetailFragment)
        }


    }

    private fun switchToSubjectDetail(actions: NavDirections, destinationId: Int){
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(actions)
        }
    }

    private fun getNavController(): NavController {
        return (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
    }


    private fun isFragmentInBackStack(destinationId: Int) =
        try {

            getNavController().getBackStackEntry(destinationId)
            true
        } catch (e: Exception) {
            false
        }


}