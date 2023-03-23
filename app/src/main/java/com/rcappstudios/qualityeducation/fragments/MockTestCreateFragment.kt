package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentMockTestCreateBinding
import com.rcappstudios.qualityeducation.databinding.RowAddFieldBinding
import com.rcappstudios.qualityeducation.model.Field
import com.rcappstudios.qualityeducation.model.Test

class MockTestCreateFragment : Fragment() {

    private lateinit var binding: FragmentMockTestCreateBinding
    private lateinit var radioAdapter: ArrayAdapter<String>
    private var database = FirebaseDatabase.getInstance()
    private var testList: MutableList<Test?> = mutableListOf()
    private var subject = "Physics"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMockTestCreateBinding.inflate(layoutInflater, container, false)

        val types = arrayOf(EditText::class.simpleName, RadioButton::class.simpleName)
        radioAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        radioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        database.getReference("Test/$subject").addChildEventListener(object : ChildEventListener{
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

        binding.testName.requestFocus()
        binding.buttonAdd.setOnClickListener {
            var preText = "Initial"
            if (binding.layoutList.size > 0) {
                preText =
                    binding.layoutList[binding.layoutList.size - 1].findViewById<EditText>(R.id.field_label).text.toString()
                        .trim()
            }
            if (preText == "" || preText.isEmpty()) {
                Toast.makeText(requireContext(), "Previous Field is empty!", Toast.LENGTH_SHORT).show()
            } else {
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
                binding.layoutList.addView(newField.root)
            }
        }

        binding.buttonCreate.setOnClickListener {
            val testName: String = binding.testName.text.toString()
            val size = binding.layoutList.size
            if (testName.isNotEmpty() && testName != "" && size > 0) {
                for (test in testList) {
                    if (testName == test?.name!!) {
                        Toast.makeText(requireContext(), "Test name already exists!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                val thisTestList = mutableListOf<Field>()
                for (i in 0 until size) {
                    val v: View = binding.layoutList[i]
                    val label = v.findViewById<EditText>(R.id.field_label)
                    val dataType = v.findViewById<Spinner>(R.id.type_spinner)
                    val testField =
                        if (dataType.selectedItem.toString() == RadioButton::class.simpleName) {
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
                }
                this.createForm(testName, thisTestList)
            } else {
//                Log.d(TAG, "onCreate: Data Upload Failed")
            }

//            Log.d("Mytag", "createButtonClickListener: $formKey")
        }

        return binding.root
    }

    private fun createForm(name: String, formFields: List<Field>) {
        val newTest = Test(name, formFields)
        database.getReference("Test/$subject/${newTest.name}/questions").setValue(newTest)
            .addOnSuccessListener {
                //To close this fragment
                Toast.makeText(requireContext(), "Test created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Unable to create test", Toast.LENGTH_SHORT).show()
            }
        Log.d("MockTestCreateTag", "createTest: ${newTest.name}")
    }

}