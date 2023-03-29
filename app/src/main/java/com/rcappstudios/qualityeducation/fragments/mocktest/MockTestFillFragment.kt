package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestFillBinding
import com.rcappstudios.qualityeducation.databinding.RowFillTestBinding
import com.rcappstudios.qualityeducation.fragments.mocktest.MockTestFillFragmentArgs
import com.rcappstudios.qualityeducation.model.Field

class MockTestFillFragment : Fragment() {

    private lateinit var binding: FragmentMockTestFillBinding
    val navArgs: MockTestFillFragmentArgs by navArgs()
    private var subject = "Physics"
    private var testName = "test"
    private var testFields: MutableList<Field> = mutableListOf()
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
        FirebaseDatabase.getInstance()
            .getReference("Test/$subject/${testName}/questions/fields")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
//                    Log.d("MYTAG", "openTest: ${it.childrenCount}")
                    for (c in it.children) {
                        Log.d("MYTAG", "onSuccess: ${c.value}")
                        testFields.add(c.getValue(Field::class.java)!!)
                    }
                    if (testFields.isNotEmpty()) {
                        addFields()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("MYTAG", "onFailure: $it")
            }

        return binding.root
    }

    private fun addFields() {
        testFields.forEach {
//            Log.d("TAGDATA", "execute: ${it.label} : ${it.dataType}")
            val testBinding = RowFillTestBinding.inflate(LayoutInflater.from(requireContext()))
            testBinding.fillTestFieldTV.text = it.label

            when (it.dataType) {
                EditText::class.simpleName -> {
                    testBinding.fillTestEt.visibility = View.VISIBLE
                }
                RadioButton::class.simpleName -> {
                    val rg = RadioGroup(requireContext())
                    rg.orientation = RadioGroup.VERTICAL
                    llParams.leftMargin = 20
                    for (item in it.items!!) {
                        val rb = RadioButton(requireContext())
                        rb.text = item
                        rb.textSize = 18F
                        rb.layoutParams = llParams
                        rg.addView(rb)
                    }
                    rg.setOnCheckedChangeListener { grp, id ->

                    }
                    testBinding.fillTestLl.addView(rg)
                }
            }

            binding.fillLinearLayout.addView(testBinding.root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}