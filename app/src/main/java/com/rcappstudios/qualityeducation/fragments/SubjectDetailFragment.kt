package com.rcappstudios.qualityeducation.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.rcappstudios.qualityeducation.MainActivity
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.AskQuestionsAdapter
import com.rcappstudios.qualityeducation.adapters.MockTestAdapter
import com.rcappstudios.qualityeducation.adapters.PeerLearningAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentSubjectDetailBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.rcappstudios.qualityeducation.model.RoomModel
import com.rcappstudios.qualityeducation.utils.Constants
import com.rcappstudios.qualityeducation.utils.LoadingDialog

class SubjectDetailFragment : Fragment() {

    val navArgs: SubjectDetailFragmentArgs by navArgs()
    private var isMentor: Boolean = false
    private lateinit var  loadingDialog: LoadingDialog
    private lateinit var binding: FragmentSubjectDetailBinding
    private lateinit var translator: Translator
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
        prepareTranslator()
        isMentor = requireActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getBoolean("isMentor", false)


//        if(FirebaseAuth.getInstance().currentUser!!.uid != "r2z2HA4PMGYJIenwaMo9tZdSQFF2"){
//            binding.createTest.visibility = View.GONE
//        }
    }



    private fun clickListener(){
        binding.doubtsExpand.setOnClickListener {
            val directions = SubjectDetailFragmentDirections.actionSubjectDetailFragmentToAskDoubtFragment(navArgs.subjectName.toString())
            switchToFragment(directions, R.id.askDoubtFragment)
        }

        binding.peersExpand.setOnClickListener {
            val directions = SubjectDetailFragmentDirections.actionSubjectDetailFragmentToPeerLearningFragment(navArgs.subjectName.toString())
            switchToFragment(directions, R.id.peerLearningFragment)
        }

        binding.createTest.setOnClickListener {
            val directions = SubjectDetailFragmentDirections.actionSubjectDetailFragmentToMockTestCreateFragment(navArgs.subjectName.toString())
            switchToFragment(directions, R.id.mockTestCreateFragment)
        }
    }

    private fun fetchDoubtsData(){
        FirebaseDatabase.getInstance()
            .getReference("Groups/${navArgs.subjectName}/Questions")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val questionsList = ArrayList<QuestionModel>()
                    for (c in it.children){
                        questionsList.add(c.getValue(QuestionModel::class.java)!!)
                    }
                    initDoubtsRvAdapter(questionsList)
                }
                loadingDialog.dismiss()
            }
    }

    private fun fetchRoomsList(){
        FirebaseDatabase.getInstance().getReference("Room/${navArgs.subjectName}")
            .get().addOnSuccessListener {
                if(it.exists()){
                    val roomList = mutableListOf<RoomModel>()
                    for(c in it.children){
                        roomList.add(c.getValue(RoomModel::class.java)!!)
                    }
                    initRv(roomList)
                }
                loadingDialog.dismiss()
            }
    }

    private fun getMockTestList(){
        FirebaseDatabase.getInstance().getReference("Test/${navArgs.subjectName.toString()}")
            .get().addOnSuccessListener {
                if(it.exists()){
                    val testList = mutableListOf<String>()
                    for(c in it.children){
                        testList.add(c.key.toString())
                    }
                    initMockTestAdapter(testList)
                }
                loadingDialog.dismiss()
            }
    }

    private fun initMockTestAdapter(testList: MutableList<String>){
        binding.mockTestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mockTestRecyclerView.adapter = MockTestAdapter(requireContext(),testList){int, test->
            val directions = SubjectDetailFragmentDirections.actionSubjectDetailFragmentToMockTestFillFragment(navArgs.subjectName, test)
            switchToFragment(directions, R.id.mockTestFillFragment)
//            SubjectDetailFragmentDirections.actionSubjectDetailFragmentToMockTestFillFragment(navArgs.subjectName, test.name)
        }
    }

    private fun initDoubtsRvAdapter(questionsList: ArrayList<QuestionModel>){
        binding.rvDoubts.setHasFixedSize(true)
        binding.rvDoubts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDoubts.adapter = AskQuestionsAdapter(requireContext(), questionsList, true){ flag, value->
            when(flag){
                2 -> switchToFragment(
                    SubjectDetailFragmentDirections.actionSubjectDetailFragmentToCommentsFragment(value),
                    R.id.commentsFragment
                )

            }
        }
    }


    private fun initRv(roomList: MutableList<RoomModel>){
        binding.rvPeers.setHasFixedSize(true)
        binding.rvPeers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPeers.adapter =
            PeerLearningAdapter(requireContext(), roomList, true){int, room->

            }
    }



    private fun switchToFragment(actions: NavDirections, destinationId: Int){
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

    private  fun prepareTranslator() {
        val sharedPreferences = requireActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getString(Constants.LANGUAGE, null)
        if (sharedPreferences != null) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(sharedPreferences)
                .build()
           translator = Translation.getClient(options)

            translator.downloadModelIfNeeded().addOnSuccessListener {
                translate()
            }.addOnFailureListener {

            }
        }
    }

    private fun translate(){
        translator.translate(binding.tvRecentDoubt.text.toString()).addOnSuccessListener {
            binding.tvRecentDoubt.text = it
        }
        translator.translate(binding.tvPeerDiscussion.text.toString()).addOnSuccessListener {
            binding.tvPeerDiscussion.text = it
        }
        translator.translate(binding.tvRecentDoubt.text.toString()).addOnSuccessListener {
            binding.tvRecentDoubt.text = it
        }
        translator.translate(binding.tvViewAll1.text.toString()).addOnSuccessListener {
            binding.tvViewAll1.text = it
        }
        translator.translate(binding.tvViewAll2.text.toString()).addOnSuccessListener {
            binding.tvViewAll2.text = it
        }
        translator.translate(binding.tvViewAll3.text.toString()).addOnSuccessListener {
            binding.tvViewAll3.text = it
        }
    }

}