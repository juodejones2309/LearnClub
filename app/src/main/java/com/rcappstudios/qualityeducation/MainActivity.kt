package com.rcappstudios.qualityeducation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.rajat.pdfviewer.PdfViewerActivity
import com.rcappstudios.qualityeducation.databinding.ActivityMainBinding
import com.rcappstudios.qualityeducation.fragments.mentors.MentorViewMessagesActivity
import com.rcappstudios.qualityeducation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private  var isMentor: Boolean = false

    companion object{
        lateinit var subject: String
        lateinit var translator : Translator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        supportActionBar!!.hide()
        setContentView(binding.root)
//        prepareTranslator()
        setUpNavigationComponent()
        checkSharedPrefData()
//        FirebaseDatabase.getInstance()
//            .getReference("Room/-NQtlKL0du600SeiAvE9/whiteBoard")
//            .removeValue()
        actionBar?.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"))

//        openPdf()
    }

    private fun checkSharedPrefData(){
        val sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)

        isMentor = sharedPref.getBoolean("isMentor", false)
        if(isMentor){
            val mentorName = sharedPref.getString(Constants.NAME, null)
            val mentorSubject = sharedPref.getString(Constants.SUBJECT, null)
            Log.d("TAGData", "checkSharedPrefData: mentorName -> $mentorName mentorSubject -> $mentorSubject")
        } else {
            val studentName = sharedPref.getString(Constants.NAME, null)
            val grade = sharedPref.getString(Constants.GRADE, null)
            Log.d("TAGData", "checkSharedPrefData: mentorName -> $studentName mentorSubject -> $grade")
        }
    }

    private fun setUpNavigationComponent(){
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setupWithNavController(getNavController())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        if(item.itemId == R.id.viewMentorMessage){
            startActivity(Intent(this, MentorViewMessagesActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(isMentor){
            menuInflater.inflate(R.menu.mentor_top_menu, menu)
        }
        return true
    }

    private fun getNavController(): NavController {
        return (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
    }

    private  fun prepareTranslator() {
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
            .getString(Constants.LANGUAGE, null)
        if (sharedPreferences != null) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(sharedPreferences)
                .build()
            translator = Translation.getClient(options)

            translator.downloadModelIfNeeded().addOnSuccessListener {
                Toast.makeText(this, "Language Download successful", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {

            }
        }
    }

    private fun openPdf(){
        startActivity(
        PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
            this,// ,
            "https://firebasestorage.googleapis.com/v0/b/qualityeducation-96caa.appspot.com/o/Pdf%2F1680193711159?alt=media&token=dbcd2185-91c8-4f6f-9ce0-53acb45e4718",                                // PDF URL in String format
            "Pdf title/name ",                        // PDF Name/Title in String format
            "",                  // If nothing specific, Put "" it will save to Downloads
            enableDownload = false                    // This param is true by defualt.
        )
        )
    }

}