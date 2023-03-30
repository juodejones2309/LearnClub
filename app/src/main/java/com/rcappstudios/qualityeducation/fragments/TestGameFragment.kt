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


class TestGameFragment : Fragment(), SubjectAdapter.SubjectClickListener {

    private lateinit var binding: FragmentTestGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTestGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchTest()
    }

    private fun fetchTest(){
        FirebaseDatabase.getInstance()
            .getReference("CommonTest").get()
            .addOnSuccessListener {
                if(it.exists()){
                    val testList = mutableListOf<String>()
                    for(c in it.children){
                        testList.add(c.getValue(Test::class.java)!!.name.toString())
                    }
                    initTestRv(testList)
                }
            }
    }

    private fun initTestRv(testList: MutableList<String>){
        binding.testNameRv.layoutManager = LinearLayoutManager(requireContext())
        binding.testNameRv.setHasFixedSize(true)
        binding.testNameRv.adapter = SubjectAdapter(requireContext(), testList, this)
    }

    override fun onSubjectClick(subject: String) {
        val intent = Intent(requireActivity(), AttendTestActivity::class.java)
        intent.putExtra("testName", subject)
        startActivity(intent)
    }
}