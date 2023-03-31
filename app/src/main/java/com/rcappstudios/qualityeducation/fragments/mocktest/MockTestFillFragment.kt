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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestFillBinding
import com.rcappstudios.qualityeducation.databinding.RowFillTestBinding
import com.rcappstudios.qualityeducation.model.AttendersData
import com.rcappstudios.qualityeducation.model.Field
import com.rcappstudios.qualityeducation.model.StudentData
import com.rcappstudios.qualityeducation.model.Test

class MockTestFillFragment : Fragment() {

    private lateinit var binding: FragmentMockTestFillBinding
    val navArgs: MockTestFillFragmentArgs by navArgs()
    private var attenders = mutableListOf<AttendersData>()
    private var subject = ""
    private var testName = "test"
    private var test: Test? = null
    private var radioSelected: String = ""
    private var testFields: List<Field> = mutableListOf()
    private var score: Int = 0
    private var student: StudentData? = null
    private var llParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        FirebaseDatabase.getInstance().getReference(
            "Students/" + "${FirebaseAuth.getInstance().currentUser?.uid}"
        ).get().addOnSuccessListener {
                if (it.exists()) {
                    student = it.getValue(StudentData::class.java)
                    student!!.testAttended = student!!.testAttended!! + 1
                    if (score > 0) {
                        student!!.score = student!!.score!! + score
                    }
                    FirebaseDatabase.getInstance().getReference("Students/" +
                            "${FirebaseAuth.getInstance().currentUser?.uid}").setValue(student)
                    test?.attenders?.add(AttendersData(student?.name, score))
//                    updateTestAttenders()
                }
            }
    }

    private fun updateTestAttenders() {

        FirebaseDatabase.getInstance()
            .getReference("Test/$subject/${test?.name}/attenders")
            .setValue(attenders)
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Test Submitted", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "Test Not Submitted", Snackbar.LENGTH_SHORT).show()
            }
    }


    private fun getScore(): Int {
        var point = 0
        for (i in 0 until binding.fillLinearLayout.size) {
            val v = binding.fillLinearLayout[i]
            val question = v.findViewById<TextView>(R.id.fill_test_fieldTV).text.toString().trim()
            val fillEt = v.findViewById<EditText>(R.id.fill_test_et)
            val radioGroup = v.findViewById<RadioGroup>(R.id.fill_test_rg)
            if (radioGroup.visibility == View.VISIBLE) {
                if (test?.answers?.get(question)!! == radioSelected) point++
            } else {
                if (test?.answers?.get(question)!! == fillEt.text.toString().trim()) point++
            }
        }
        if (point > 0) {
            score = (point/binding.fillLinearLayout.size)*100
        }
        return score
    }

    private fun getTest() {
        FirebaseDatabase.getInstance().getReference("Test/$subject/${testName}").get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val t = it.getValue(Test::class.java)
                    if (t != null) test = t
                    testFields = t?.fields!!
                    if (testFields.isNotEmpty()) {
                        addFields()
                    }
                }
            }.addOnFailureListener {
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
                        var cId = id % items.size
                        if (cId == 0) cId = items.size
                        radioSelected = items[cId - 1]
                        Log.d("MyTagData", "addFields: $radioSelected")
                    }
                }
            }

            binding.fillLinearLayout.addView(testBinding.root)
        }
    }

}