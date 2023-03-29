package com.rcappstudios.qualityeducation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.databinding.ActivityAttendTestBinding
import com.rcappstudios.qualityeducation.databinding.RowFillTestBinding
import com.rcappstudios.qualityeducation.model.Field
import com.rcappstudios.qualityeducation.model.Test

class AttendTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendTestBinding
    private lateinit var testName: String
    private lateinit var test: Test
    private var testFields: List<Field> = mutableListOf()
    private var radioSelected: String = ""
    private var llParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        testName = intent.getStringExtra("testName")!!
        fetchTestDetails()
        binding.submitButton.setOnClickListener {
            updateDatabase()
        }
    }

    private fun updateDatabase() {
        FirebaseDatabase.getInstance()
            .getReference("Leaderboard/${FirebaseAuth.getInstance().currentUser!!.uid}")
            .setValue(getScore())
            .addOnSuccessListener {

            }
    }

    private fun fetchTestDetails(){
        FirebaseDatabase.getInstance()
            .getReference("Test/${testName}")
            .get().addOnSuccessListener {
                if(it.exists()){
                    test = it.getValue(Test::class.java)!!
                    testFields = test.fields!!
                    if (testFields.isNotEmpty()) {
                        addFields()
                    }
                }
            }
    }

    private fun addFields() {
        testFields.forEach {
//            Log.d("TAGDATA", "execute: ${it.label} : ${it.dataType}")
            val testBinding = RowFillTestBinding.inflate(LayoutInflater.from(this))
            testBinding.fillTestFieldTV.text = it.label

            testBinding.fillTestRg.visibility = View.VISIBLE
            testBinding.fillTestRg.orientation = RadioGroup.VERTICAL
            testBinding.fillTestRg.clearCheck()
            llParams.leftMargin = 20
            for (item in it.items!!) {
                val rb = RadioButton(this)
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
            binding.linearLayout.addView(testBinding.root)
        }
    }

    private fun getScore(): Int {
        var score = 0
        for (i in 0 until binding.linearLayout.size) {
            val v = binding.linearLayout[i]
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

}