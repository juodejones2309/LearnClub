package com.rcappstudios.qualityeducation.fragments.mentors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.ActivityMentorSupportBinding
import com.rcappstudios.qualityeducation.utils.Constants


class MentorSupportActivity : AppCompatActivity() {

    companion object{
        @JvmStatic
        lateinit var mentorUserId: String
        @JvmStatic
        lateinit var subject: String
        @JvmStatic
        lateinit var studentUserId: String
        var isMentor: Boolean = false
    }

    private lateinit var binding: ActivityMentorSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentorSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isMentor = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getBoolean("isMentor", true)

        if(isMentor){
            //Intent Coming from MentorViewMessageActivity
            studentUserId = intent.getStringExtra("studentUserId")!!
            mentorUserId = FirebaseAuth.getInstance().uid.toString()
            subject = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
                .getString("subject", "Physics")!!
        } else {
            //Intent Coming from MentorFragment
            mentorUserId = intent.getStringExtra("mentorUserId")!!
            studentUserId = FirebaseAuth.getInstance().uid.toString()
            subject = intent.getStringExtra("subject")!!
        }

        setupBottomNavigation()
        binding.bottomBar.setItemSelected(R.id.roomWhiteBoard)
    }

    private fun setupBottomNavigation(){
        binding.bottomBar.setOnItemSelectedListener { item ->
            when(item){
                R.id.roomWhiteBoard->{
                    switchToFragment(R.id.mentorWhiteBoardFragment)
                }
                R.id.roomVideoStream->{
                    switchToFragment(R.id.mentorVideoFragment)
                }
                R.id.roomChat->{
                    switchToFragment(R.id.mentorChatFragment)
                }
            }
        }
    }

    private fun getNavController(): NavController {
        return (supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment).navController
    }

    private fun switchToFragment(destinationId: Int) {
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(destinationId)
        }
    }

    private fun isFragmentInBackStack(destinationId: Int) =
        try {
            getNavController().getBackStackEntry(destinationId)
            true
        } catch (e: Exception) {
            false
        }

    override fun onBackPressed() {
        if(binding.bottomBar.getSelectedItemId() == R.id.roomChat){
            super.onBackPressed()
        } else {
            binding.bottomBar.setItemSelected(R.id.roomChat)
        }

    }
//    private fun
}