package com.rcappstudios.qualityeducation.fragments.mocktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.adapters.SubjectAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestResultBinding

class MockTestResultFragment : Fragment() {

    private lateinit var binding: FragmentMockTestResultBinding
    private val navArgs: MockTestResultFragmentArgs by navArgs()
    private var resultMap: MutableLiveData<Map<String, Int>> = MutableLiveData()
    private lateinit var subjectAdapter: SubjectAdapter
    private var subjectName = ""
    private var testName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMockTestResultBinding.inflate(layoutInflater, container, false)
        subjectName = navArgs.subjectName
        testName = navArgs.testName
        getTestResult()
        initRvAdapter()
        return binding.root
    }

    private fun initRvAdapter() {
        binding.testResultRv.layoutManager = LinearLayoutManager(requireContext())
        subjectAdapter = SubjectAdapter(requireContext(), null,
            mapOf("Jones" to 65, "Hari" to 80, "James" to 35), null)
        binding.testResultRv.adapter = subjectAdapter
    }

    private fun getTestResult() {
        FirebaseDatabase.getInstance()
            .getReference("Test/$subjectName/$testName/attenders")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val map = mutableMapOf<String, Int>()
                    for (c in it.children) {
                        map[c.key.toString()] = c.value.toString().toInt()
                    }
                    resultMap.postValue(map)
                }
            }
    }
}