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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestCreateBinding
import com.rcappstudios.qualityeducation.databinding.RowAddFieldBinding
import com.rcappstudios.qualityeducation.fragments.SubjectDetailFragmentArgs
import com.rcappstudios.qualityeducation.model.Field
import com.rcappstudios.qualityeducation.model.Test

class MockTestCreateFragment : Fragment() {

    private lateinit var binding: FragmentMockTestCreateBinding
    private lateinit var radioAdapter: ArrayAdapter<String>
    private var database = FirebaseDatabase.getInstance()
    private var testList: MutableList<Test?> = mutableListOf()

    val navArgs: SubjectDetailFragmentArgs by navArgs()
    private lateinit var subject: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMockTestCreateBinding.inflate(layoutInflater, container, false)
        subject = navArgs.subjectName.toString()
        initSpinner()
        getTestList()
        binding.testName.requestFocus()
        clickListener()

        return binding.root
    }

    private fun clickListener() {
        binding.buttonAdd.setOnClickListener {
            addButtonClickListener()
        }

        binding.buttonCreate.setOnClickListener {
            createButtonClickListener()
        }
    }

    private fun initSpinner() {
        val types = arrayOf("Fillup", "MCQ")
        radioAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        radioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun getTestList() {
        database.getReference("Test/$subject").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MockTestCreateTag", "onChildAdded: ${snapshot.key}")
                testList.add(snapshot.getValue(Test::class.java))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MockTestCreateTag", "onChildChanged: ${snapshot.key}")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val form = snapshot.getValue(Test::class.java)
                Log.d("MockTestCreateTag", "onChildRemoved: ${snapshot.key}")
                testList.remove(form)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MockTestCreateTag", "onChildMoved: ${snapshot.key}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("MockTestCreateTag", "onCancelled: ${error.message}")
            }

        })
    }

    private fun createButtonClickListener() {
        val testName: String = binding.testName.text.toString()
        val size = binding.layoutList.size
        if (testName.isNotEmpty() && testName != "" && size > 0) {
            if (testList.isNotEmpty()) {
                for (test in testList) {
                    if (testName == test?.name!!) {
                        Toast.makeText(
                            requireContext(),
                            "Test name already exists!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
            }
            val thisTestList = mutableListOf<Field>()
            val answerMap = mutableMapOf<String, String>()
            for (i in 0 until size) {
                val v: View = binding.layoutList[i]
                val label = v.findViewById<EditText>(R.id.field_label)
                val dataType = v.findViewById<Spinner>(R.id.type_spinner)
                val answer = v.findViewById<EditText>(R.id.answer_et)
                val testField =
                    if (dataType.selectedItem.toString() == "MCQ") {
                        val radioItems =
                            v.findViewById<EditText>(R.id.radio_list_et).text.toString().trim()
                                .split(',')
                        Field(
                            label.text!!.toString().trim(),
                            dataType.selectedItem.toString().trim(),
                            radioItems
                        )
                    } else {
                        Field(
                            label.text!!.toString().trim(),
                            dataType.selectedItem.toString().trim()
                        )
                    }
//                    Log.d(TAG, "onCreate: $formField")
                thisTestList.add(testField)
                answerMap[testField.label!!] = answer.text!!.toString().trim()
            }
            this.createTest(testName, thisTestList, answerMap)
        } else {
//                Log.d(TAG, "onCreate: Data Upload Failed")
        }

//            Log.d("Mytag", "createButtonClickListener: $formKey")
    }

    private fun addButtonClickListener() {
        var preText = "Initial"
        var preAns = "Empty"
        if (binding.layoutList.size > 0) {
            preText = binding.layoutList[binding.layoutList.size - 1]
                    .findViewById<EditText>(R.id.field_label).text.toString()
                    .trim()
            preAns = binding.layoutList[binding.layoutList.size - 1]
                .findViewById<EditText>(R.id.answer_et).text.toString()
                .trim()
        }
        if (preText == "" || preText.isEmpty()) {
            Snackbar.make(binding.root, "Previous Field is empty!", Snackbar.LENGTH_SHORT).show()
        } else if (preAns == "" || preAns.isEmpty() ) {
            Snackbar.make(binding.root, "Previous Field Answer is empty!", Snackbar.LENGTH_SHORT).show()
        } else {
            binding.layoutList.addView(createNewField())
        }
    }

    private fun createNewField(): View {
        val newField = RowAddFieldBinding.inflate(LayoutInflater.from(requireContext()))
        newField.fieldLabel.requestFocus()
        newField.typeSpinner.adapter = radioAdapter
        newField.typeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 1) {
                    newField.radioListEt.visibility = View.VISIBLE
                } else {
                    newField.radioListEt.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                        TODO("Not yet implemented")
            }

        }
        newField.rowAddDeleteBt.setOnClickListener {
            binding.layoutList.removeView(newField.root)
        }
        return newField.root
    }

    private fun createTest(name: String, testFields: List<Field>, answers: Map<String, String>) {
        val newTest = Test(name, testFields, FirebaseAuth.getInstance().currentUser?.uid, answers,
            mutableListOf()
        )
        database.getReference("Test/$subject/${newTest.name}").setValue(newTest)
            .addOnSuccessListener {
                //To close this fragment
                Toast.makeText(requireContext(), "Test created", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Unable to create test", Toast.LENGTH_SHORT).show()
            }
        Log.d("MockTestCreateTag", "createTest: ${newTest.name}")
    }

}