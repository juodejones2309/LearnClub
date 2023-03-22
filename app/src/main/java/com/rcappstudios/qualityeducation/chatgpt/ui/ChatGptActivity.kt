package com.zero.chatgpt_androidapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.ActivityChatgptBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatGptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatgptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatgptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        initBottomNavigationView()
    }

    private fun initBottomNavigationView(){
        binding.bottomNavMenu.setItemSelected(R.id.completion)
        binding.bottomNavMenu.setOnItemSelectedListener { item ->
            when(item){
                R.id.completion->{
                    switchToFragment(R.id.completionFragment)
                }
                R.id.ivGenerator->{
                    switchToFragment(R.id.imageGeneratorFragment)
                }

            }
        }
    }

    private fun getNavController(): NavController {
        return (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
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
        super.onBackPressed()
        binding.bottomNavMenu.setItemSelected(R.id.completion)
    }

}