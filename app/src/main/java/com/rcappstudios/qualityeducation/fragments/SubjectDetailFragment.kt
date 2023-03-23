package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.AskQuestionsAdapter
import com.rcappstudios.qualityeducation.adapters.MockTestAdapter
import com.rcappstudios.qualityeducation.adapters.PeerLearningAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentSubjectDetailBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.rcappstudios.qualityeducation.model.RoomModel
import com.rcappstudios.qualityeducation.model.Test
import com.rcappstudios.qualityeducation.utils.LoadingDialog

class SubjectDetailFragment : Fragment() {

    val navArgs: SubjectDetailFragmentArgs by navArgs()
    private lateinit var  loadingDialog: LoadingDialog
    private lateinit var binding: FragmentSubjectDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubjectDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog =  LoadingDialog(requireActivity(),"Loading Please wait...." )
        clickListener()
        loadingDialog.startLoading()
        fetchDoubtsData()
        fetchRoomsList()
        getMockTestList()
    }

    private fun clickListener(){
        binding.doubtsExpand.setOnClickListener {
            switchToFragment(R.id.askDoubtFragment)
        }

        binding.peersExpand.setOnClickListener {
            switchToFragment(R.id.peerLearningFragment)
        }

        binding.createTest.setOnClickListener {
            val directions = SubjectDetailFragmentDirections.actionSubjectDetailFragmentToMockTestCreateFragment(navArgs.subjectName.toString())
            switchToCommentsFragment(directions, R.id.mockTestCreateFragment)
        }
    }

    private fun fetchDoubtsData(){
        FirebaseDatabase.getInstance()
            .getReference("Questions")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val questionsList = ArrayList<QuestionModel>()
                    for (c in it.children){
                        questionsList.add(c.getValue(QuestionModel::class.java)!!)
                    }
                    initDoubtsRvAdapter(questionsList)
                    loadingDialog.dismiss()
                }
            }
    }

    private fun fetchRoomsList(){
        FirebaseDatabase.getInstance().getReference("Room")
            .get().addOnSuccessListener {
                if(it.exists()){
                    val roomList = mutableListOf<RoomModel>()
                    for(c in it.children){
                        roomList.add(c.getValue(RoomModel::class.java)!!)
                    }
                    initRv(roomList)
                    loadingDialog.dismiss()
                }
            }
    }

    private fun getMockTestList(){
        FirebaseDatabase.getInstance().getReference("Test/${navArgs.subjectName.toString()}")
            .get().addOnSuccessListener {
                if(it.exists()){
                    val testList = mutableListOf<Test>()
                    for(c in it.children){
                        testList.add(c.getValue(Test::class.java)!!)
                    }
                    initMockTestAdapter(testList)
                }
            }
    }

    private fun initMockTestAdapter(testList: MutableList<Test>){
        binding.mockTestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mockTestRecyclerView.adapter = MockTestAdapter(requireContext(),testList){int, test->
//           SubjectDetailFragmentDirections.actionSubjectDetailFragmentToMockTestFillFragment(navArgs.subjectName, test.name)
        }
    }

    private fun initDoubtsRvAdapter(questionsList: ArrayList<QuestionModel>){
        binding.rvDoubts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDoubts.adapter = AskQuestionsAdapter(requireContext(), questionsList){ flag, value->
            when(flag){
                2 -> switchToCommentsFragment(
                    SubjectDetailFragmentDirections.actionSubjectDetailFragmentToCommentsFragment(value),
                    R.id.commentsFragment
                )

            }
        }
    }


    private fun initRv(roomList: MutableList<RoomModel>){
        binding.rvPeers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPeers.adapter =
            PeerLearningAdapter(requireContext(), roomList){int, room->

            }
    }



    private fun switchToCommentsFragment(actions: NavDirections, destinationId: Int){
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(actions)
        }
    }

    private fun switchToFragment(destinationId: Int) {
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(destinationId)
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