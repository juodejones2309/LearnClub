package com.rcappstudios.qualityeducation.fragments.mocktest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestFillBinding
import com.rcappstudios.qualityeducation.databinding.RowFillTestBinding
import com.rcappstudios.qualityeducation.fragments.mocktest.MockTestFillFragmentArgs
import com.rcappstudios.qualityeducation.model.Field
import com.rcappstudios.qualityeducation.model.StudentData
import com.rcappstudios.qualityeducation.model.Test

class MockTestFillFragment : Fragment() {

    private lateinit var binding: FragmentMockTestFillBinding
    val navArgs: MockTestFillFragmentArgs by navArgs()
    private var subject = "Physics"
    private var testName = "test"
    private var test: Test? = null
    private var radioSelected: String = ""
    private var testFields: List<Field> = mutableListOf()
    private var llParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMockTestFillBinding.inflate(inflater, container, false)
        subject = navArgs.subjectName!!
        testName = navArgs.testName!!
        getTest()
        binding.submitButton.setOnClickListener {
            submitButtonClickListener()
        }

        return binding.root
    }

    private fun submitButtonClickListener() {
        updateStudentDB(getScore())
    }

    private fun updateStudentDB(score: Int) {
        var student: StudentData? = null
        FirebaseDatabase.getInstance().getReference("Students/" +
                "${FirebaseAuth.getInstance().currentUser?.uid}").get()
            .addOnSuccessListener {
                if (it.exists()) {
                    student = it.getValue(StudentData::class.java)
                    student!!.testAttended = student!!.testAttended!! + 1
                    if (score > 0) {
                        student!!.score = student!!.score!! + score
                    }
                    FirebaseDatabase.getInstance().getReference("Students/" +
                            "${FirebaseAuth.getInstance().currentUser?.uid}").setValue(student)
                }
            }

       /* if (student != null) {
            Log.d("StudentSimulation", "updateStudentDB: ${student!!.name}")
            student!!.testAttended = student!!.testAttended!! + 1
            if (score > 0) {
                student!!.score = student!!.score!! + score
            }
            FirebaseDatabase.getInstance().getReference("Students/" +
                    "${FirebaseAuth.getInstance().currentUser?.uid}").setValue(student)
        }*/
    }

    private fun getScore(): Int {
        var score = 0
        for (i in 0 until binding.fillLinearLayout.size) {
            val v = binding.fillLinearLayout[i]
            val question = v.findViewById<TextView>(R.id.fill_test_fieldTV).text.toString().trim()
            val fillEt = v.findViewById<EditText>(R.id.fill_test_et)
            val radioGroup = v.findViewById<RadioGroup>(R.id.fill_test_rg)
            if (radioGroup.visibility == View.VISIBLE) {
                if (test?.answers?.get(question)!! == radioSelected) score++
            } else {
                if (test?.answers?.get(question)!! == fillEt.text.toString().trim()) score++
            }
        }
        return score
    }

    private fun getTest() {
        FirebaseDatabase.getInstance()
            .getReference("Test/$subject/${testName}")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
/*
                    Log.d("MYTAG", "openTest: ${it.childrenCount}")
                    for (c in it.children) {
                        Log.d("MYTAG", "onSuccess: ${c.value}")
                        testFields.add(c.getValue(Field::class.java)!!)
                    }
                    if (testFields.isNotEmpty()) {
                        addFields()
                    }
*/
                    val t = it.getValue(Test::class.java)
                    if (t!=null) test = t
                    testFields = t?.fields!!
                    if (testFields.isNotEmpty()) {
                        addFields()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("MYTAG", "onFailure: $it")
            }

    }

    private fun addFields() {
        testFields.forEach {
//            Log.d("TAGDATA", "execute: ${it.label} : ${it.dataType}")
            val testBinding = RowFillTestBinding.inflate(LayoutInflater.from(requireContext()))
            testBinding.fillTestFieldTV.text = it.label

            when (it.dataType) {
                "Fillup" -> {
                    testBinding.fillTestEt.visibility = View.VISIBLE
                }
                "MCQ" -> {
                    testBinding.fillTestRg.visibility = View.VISIBLE
                    testBinding.fillTestRg.orientation = RadioGroup.VERTICAL
                    testBinding.fillTestRg.clearCheck()
                    llParams.leftMargin = 20
                    for (item in it.items!!) {
                        val rb = RadioButton(requireContext())
                        rb.text = item
                        rb.textSize = 18F
                        rb.layoutParams = llParams
                        testBinding.fillTestRg.addView(rb)
                    }
                    testBinding.fillTestRg.setOnCheckedChangeListener { grp, id ->
                        val items = it.items
                        var cId = id%items.size
                        if (cId==0) cId=items.size
                        radioSelected = items[cId-1 ]
                        Log.d("MyTagData", "addFields: $radioSelected")
                    }
                }
            }

            binding.fillLinearLayout.addView(testBinding.root)
        }
    }

}