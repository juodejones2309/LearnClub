package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.AskQuestionsAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentAskDoubtBinding
import com.rcappstudios.qualityeducation.model.QuestionModel


class AskDoubtFragment : Fragment() {


    private lateinit var binding: FragmentAskDoubtBinding
    private lateinit var adapter: AskQuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAskDoubtBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
        fetchData()
    }

    private fun fetchData(){
        FirebaseDatabase.getInstance()
            .getReference("Questions")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val questionsList = ArrayList<QuestionModel>()
                    for (c in it.children){
                        questionsList.add(c.getValue(QuestionModel::class.java)!!)
                    }
                    initRvAdapter(questionsList)
                }
            }
    }

    private fun initRvAdapter(questionsList: ArrayList<QuestionModel>){
        binding.rvQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuestions.adapter = AskQuestionsAdapter(requireContext(), questionsList){ flag, value->
            when(flag){
                2 -> switchToCommentsFragment(
                    AskDoubtFragmentDirections.actionAskDoubtFragmentToCommentsFragment(value),
                    R.id.commentsFragment
                )

            }
        }
    }

    private fun clickListener(){
        binding.fabAddQuestion.setOnClickListener {
            switchToFragment(R.id.postQuestionFragment)
        }
    }

    fun switchToCommentsFragment(actions: NavDirections, destinationId: Int){
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(actions)
        }
    }

    private fun getNavController(): NavController {
        return (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
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



}