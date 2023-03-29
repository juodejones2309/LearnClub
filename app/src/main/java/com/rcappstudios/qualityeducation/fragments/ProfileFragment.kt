package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.nl.translate.TranslateLanguage
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentProfileBinding
import com.rcappstudios.qualityeducation.utils.Constants

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var languageAdapter : ArrayAdapter<CharSequence>

    private var selectedLanguage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLangAdapter()
    }

    private fun initLangAdapter(){
        languageAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_language,
            R.layout.spinner_layout
        )
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.adapter = languageAdapter;

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedLanguage = binding.languageSpinner.selectedItem.toString()
                val parentId : Int = adapterView!!.id

                if(parentId == R.id.languageSpinner){
                    when(selectedLanguage){

                        "தமிழ்"->{
                            requireActivity().getSharedPreferences(
                                Constants.SHARED_PREF,
                                AppCompatActivity.MODE_PRIVATE
                            ).edit().apply {
                                putString(Constants.LANGUAGE, TranslateLanguage.TAMIL)
                                Snackbar.make(binding.root, "Restart to see தமிழ் content", Toast.LENGTH_LONG).show()
                            }.commit()
                        }

                        "English"->{
                            requireActivity().getSharedPreferences(Constants.SHARED_PREF,
                                AppCompatActivity.MODE_PRIVATE
                            ).edit().apply {
                                putString(Constants.LANGUAGE, TranslateLanguage.ENGLISH)
                                Snackbar.make(binding.root, "Restart to see English content", Toast.LENGTH_LONG).show()

                            }.commit()

                        }

                        "हिन्दी"->{
                            requireActivity().getSharedPreferences(Constants.SHARED_PREF,
                                AppCompatActivity.MODE_PRIVATE
                            ).edit().apply {
                                putString(Constants.LANGUAGE, TranslateLanguage.HINDI)
                                Snackbar.make(binding.root, "Restart to see हिन्दी content", Toast.LENGTH_LONG).show()

                            }.commit()

                        }

                        "తెలుగు"->{
                            requireActivity().getSharedPreferences(Constants.SHARED_PREF,
                                AppCompatActivity.MODE_PRIVATE
                            ).edit().apply {
                                putString(Constants.LANGUAGE, TranslateLanguage.TELUGU)
                                Snackbar.make(binding.root, "Restart to see తెలుగు content", Toast.LENGTH_LONG).show()
                            }.commit()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

}