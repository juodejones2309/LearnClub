package com.rcappstudios.qualityeducation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.rcappstudio.indoorfarming.api.RetrofitInstance
import com.rcappstudio.placesapi.youtubeDataModel.YoutubeResults
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.AskQuestionsAdapter
import com.rcappstudios.qualityeducation.adapters.YoutubeAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentAskDoubtBinding
import com.rcappstudios.qualityeducation.model.QuestionModel
import com.rcappstudios.qualityeducation.utils.Constants
import retrofit2.HttpException
import java.io.IOException


class AskDoubtFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: FragmentAskDoubtBinding
    private lateinit var adapter: AskQuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAskDoubtBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        clickListener()
        fetchData()
        initBottomSheet("Leo")
    }

    private fun fetchData(){
        FirebaseDatabase.getInstance()
            .getReference("Questions")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val questionsList = ArrayList<QuestionModel>()
                    for (c in it.children){
                        questionsList.add(c.getValue(QuestionModel::class.java)!!)
                    }
                    initRvAdapter(questionsList)
                }
            }
    }

    private fun initRvAdapter(questionsList: ArrayList<QuestionModel>){
        binding.rvQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuestions.adapter = AskQuestionsAdapter(requireContext(), questionsList){ flag, value->
            when(flag){
                2 -> switchToCommentsFragment(
                    AskDoubtFragmentDirections.actionAskDoubtFragmentToCommentsFragment(value),
                    R.id.commentsFragment
                )
                1->ytBottomState(value)

            }
        }
    }

    private fun clickListener(){
        binding.fabAddQuestion.setOnClickListener {
            switchToFragment(R.id.postQuestionFragment)
        }
    }

    fun switchToCommentsFragment(actions: NavDirections, destinationId: Int){
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(actions)
        }
    }

    private fun getNavController(): NavController {
        return (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
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

    private fun ytBottomState(value: String){
        val state = if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.STATE_COLLAPSED
        } else {
                //TODO : Setup api call and recycler view adapter
                initBottomSheet(value)
                BottomSheetBehavior.STATE_EXPANDED
            }
        bottomSheetBehavior.state = state

    }
    private fun initBottomSheet(dataString : String){
        Log.d("YouTubeData", "initBottomSheet: data accessed")
        lifecycleScope.launchWhenStarted {
            val response = try {
                RetrofitInstance.youtubeApi.getYoutubeResults("snippet", dataString, Constants.YOUTUBE_API_KEY)
            } catch (e: IOException) {

                return@launchWhenStarted

            } catch (e: HttpException) {
                return@launchWhenStarted
            }

            if (response.isSuccessful && response.body() != null) {
               setUpYtBottomRv(response.body()!!)
            }
        }
    }

    private fun setUpYtBottomRv(youtubeResults: YoutubeResults) {
        binding.bottomSheet.rvYoutubeThumbnail.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bottomSheet.rvYoutubeThumbnail.setHasFixedSize(true)
        binding.bottomSheet.rvYoutubeThumbnail.adapter =
            YoutubeAdapter(requireContext(), youtubeResults.items!!) { item, pos ->
//            watchYoutubeVideo(item)
            }
    }
}