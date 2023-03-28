package com.rcappstudios.qualityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.databinding.ActivityOtpBinding
import com.rcappstudios.qualityeducation.model.StudentData
import com.rcappstudios.qualityeducation.utils.Constants
import com.rcappstudios.qualityeducation.utils.LoadingDialog
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var verificationID  : String
    private lateinit var phoneNumber: String
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        loadingDialog = LoadingDialog(this, "Loading Please Wait....")
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListeners()
    }

    private fun clickListeners(){
        binding.btnSendOtp.setOnClickListener {
            extractNumber()
        }

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }

        binding.btnNext.setOnClickListener {
            storeStudentDetails()
        }
    }

    private fun extractNumber(){
        loadingDialog.startLoading()
        if(binding.etNumber.text.toString().isNotEmpty()){
            phoneNumber = binding.etNumber.text.toString().trim()
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber("+91"+binding.etNumber.text.toString().trim())
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

    private fun verifyOtp(){
        loadingDialog.startLoading()
        if(verificationID.isNotEmpty() && binding.etOtp.text.toString().isNotEmpty() ){
            val phoneAuthCredential = PhoneAuthProvider.getCredential(
                verificationID, binding.etOtp.text.toString().trim()
            )
            signInInWithCredential(phoneAuthCredential)
        } else {
            loadingDialog.dismiss()
            Snackbar.make(binding.root, "CHeck the OTP", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun signInInWithCredential(phoneAuthCredential: PhoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    loadingDialog.dismiss()
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

    private fun checkDatabase(){
        FirebaseDatabase.getInstance().getReference("Students/${FirebaseAuth.getInstance().uid}")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
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
                   showEnterStudentDetails()
                }
            }
    }

    private fun showEnterStudentDetails(){
        binding.detailsView.visibility = View.VISIBLE
        binding.enterOtpView.visibility = View.GONE
    }

    private fun storeStudentDetails(){
        if(binding.etName.text.isNotEmpty() && binding.etGrade.text.isNotEmpty()){
            FirebaseDatabase.getInstance().getReference("Students/${FirebaseAuth.getInstance().uid}")
                .setValue(StudentData(binding.etName.text.toString(), binding.etGrade.text.toString(), phoneNumber))
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
        } else {

        }
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}