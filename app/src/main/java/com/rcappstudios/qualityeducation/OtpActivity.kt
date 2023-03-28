package com.rcappstudios.qualityeducation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.databinding.ActivityOtpBinding
import com.rcappstudios.qualityeducation.model.StudentData
import com.rcappstudios.qualityeducation.model.mentors.MentorData
import com.rcappstudios.qualityeducation.utils.Constants
import com.rcappstudios.qualityeducation.utils.LoadingDialog
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var verificationID: String
    private lateinit var phoneNumber: String
    private lateinit var loadingDialog: LoadingDialog
    private var isStudent: Boolean = true
    private var subject: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        loadingDialog = LoadingDialog(this, "Loading Please Wait....")
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListeners()
        initSpinner()
    }

    private fun initSpinner() {
        val subjects = arrayOf("English", "Physics", "Computer Science", "Maths", "Chemistry")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.subjectSpinner.adapter = arrayAdapter
        binding.subjectSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 0 && position <= subjects.size) subject = subjects[position]
                if (subject.equals("Computer Science")) subject = "ComputerScience"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun clickListeners() {
        binding.btnSendOtp.setOnClickListener {
            if (!subject.isNullOrEmpty())
                extractNumber()
            else
                Snackbar.make(binding.root, "Please select Subject", Snackbar.LENGTH_LONG).show()
        }

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }

        binding.btnNext.setOnClickListener {
            storeDetails()
        }
        binding.stdCard.setOnClickListener {
            isStudent = true
            binding.userTypeLayout.visibility = View.GONE
            binding.sendOtpView.visibility = View.VISIBLE
        }
        binding.mentorCard.setOnClickListener {
            isStudent = false
            binding.userTypeLayout.visibility = View.GONE
            binding.sendOtpView.visibility = View.VISIBLE
        }
    }

    private fun extractNumber() {
        loadingDialog.startLoading()
        if (binding.etNumber.text.toString().isNotEmpty() && binding.etNumber.text.toString()
                .trim().length == 10
        ) {
            phoneNumber = binding.etNumber.text.toString().trim()
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber("+91" + binding.etNumber.text.toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            Snackbar.make(binding.root, "Please enter valid number", Snackbar.LENGTH_LONG).show()
        }
    }

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            loadingDialog.dismiss()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                loadingDialog.dismiss()
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {

                loadingDialog.dismiss()
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
            loadingDialog.dismiss()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            loadingDialog.dismiss()
            verificationID = verificationId
            binding.sendOtpView.visibility = View.GONE
            binding.enterOtpView.visibility = View.VISIBLE
            Toast.makeText(applicationContext, "Code sent!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtp() {
        loadingDialog.startLoading()
        if (verificationID.isNotEmpty() && binding.etOtp.text.toString().isNotEmpty()) {
            val phoneAuthCredential = PhoneAuthProvider.getCredential(
                verificationID, binding.etOtp.text.toString().trim()
            )
            signInInWithCredential(phoneAuthCredential)
        } else {
            loadingDialog.dismiss()
            Snackbar.make(binding.root, "CHeck the OTP", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun signInInWithCredential(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingDialog.dismiss()
                    getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                        .edit().putBoolean("isMentor", !isStudent).apply()
                    checkDatabase()
                } else {
                    loadingDialog.dismiss()
                }
            }
            .addOnFailureListener {
                loadingDialog.dismiss()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun checkDatabase() {
        if (isStudent) {
            checkStudentDB()
        } else if (!subject.isNullOrEmpty()) {
            checkMentorDB()
        }
    }

    private fun checkMentorDB() {
        FirebaseDatabase.getInstance()
            .getReference("Mentors/$subject/${FirebaseAuth.getInstance().uid}")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val mentor = it.getValue(MentorData::class.java)
                    getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                        .edit()
                        .putString(Constants.NAME, mentor!!.mentorName)
                        .putString(Constants.SUBJECT, mentor.subject)
                        .apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    showEnterDetails()
                }
            }
    }

    private fun checkStudentDB() {
        FirebaseDatabase.getInstance().getReference("Students/${FirebaseAuth.getInstance().uid}")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val student = it.getValue(StudentData::class.java)
                    getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                        .edit()
                        .putString(Constants.NAME, student!!.name)
                        .putString(Constants.GRADE, student.grade.toString())
                        .apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    showEnterDetails()
                }
            }
    }

    private fun showEnterDetails() {
        binding.detailsView.visibility = View.VISIBLE
        if (isStudent) binding.etGrade.visibility = View.VISIBLE
        binding.enterOtpView.visibility = View.GONE
    }

    private fun storeDetails() {
        if (binding.etName.text.isNotEmpty()) {
            if (isStudent) {
                if (binding.etGrade.text.isNotEmpty())
                    storeStudentDetails()
            } else {
                storeMentorDetails()
            }
        } else {

        }
    }

    private fun storeStudentDetails() {
        FirebaseDatabase.getInstance()
            .getReference("Students/${FirebaseAuth.getInstance().uid}")
            .setValue(
                StudentData(
                    binding.etName.text.toString(),
                    binding.etGrade.text.toString(),
                    phoneNumber
                )
            )
            .addOnSuccessListener {
                getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                    .edit()
                    .putString(Constants.NAME, binding.etName.text.toString())
                    .putString(Constants.GRADE, binding.etGrade.text.toString())
                    .apply()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun storeMentorDetails() {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance()
            .getReference("Mentors/$subject/$uid")
            .setValue(
                MentorData(
                    binding.etName.text.toString(),
                    phoneNumber,
                    subject,
                    uid
                )
            )
            .addOnSuccessListener {
                getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                    .edit()
                    .putString(Constants.NAME, binding.etName.text.toString())
                    .putString(Constants.SUBJECT, subject)
                    .apply()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                finish()
            }
    }


    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}