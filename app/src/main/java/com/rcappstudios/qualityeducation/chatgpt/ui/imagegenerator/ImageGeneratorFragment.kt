package com.zero.chatgpt_androidapp.ui.imagegenerator

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.databinding.FragmentImageGeneratorBinding
import com.zero.chatgpt_androidapp.adapter.CompletionAdapter
import com.zero.chatgpt_androidapp.adapter.ImageGenerationAdapter
import com.zero.chatgpt_androidapp.adapter.model.CompletionAdapterModel
import com.zero.chatgpt_androidapp.adapter.model.ImageGenerationAdapterModel
import com.zero.chatgpt_androidapp.data.completion.model.image.request.ImageGenerationRequest
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageGeneratorFragment : Fragment() {

    private lateinit var binding: FragmentImageGeneratorBinding
    private lateinit var viewModel: ImageGeneratorViewModel
    private lateinit var adapter: ImageGenerationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageGeneratorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ImageGeneratorViewModel::class.java]
        initAdapter()
        clickListener()
    }

    private fun initAdapter(){
        adapter = ImageGenerationAdapter(requireContext(), mutableListOf())
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChat.adapter = adapter
//        adapter.addImageGeneratedData(createAdapterModel("Type any thing.....\uD83D\uDE03", true))
        attachObserver()
    }

    private fun clickListener(){
        binding.btnAsk.setOnClickListener {
            if(binding.etQuestion.text.toString() != ""){
                viewModel.imageGenerateRequestLiveData.value = createRequest(binding.etQuestion.text.toString())
                adapter.addImageGeneratedData(createAdapterModel(binding.etQuestion.text.toString(),false))
                binding.etQuestion.text.clear()
                hideKeyboard(requireActivity())
                binding.rvChat.scrollToPosition(adapter.itemCount -1 )
            } else {
                Toast.makeText(requireContext(),"Please ask question to continue", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun attachObserver(){
        viewModel.imageGenerationResult.observe(viewLifecycleOwner){
            Log.d("TAGData", "attachObserver: $it")
            if(it.data?.get(0)!!.url != null)
                adapter.addImageGeneratedData(createAdapterModel(it.data?.get(0)!!.url,true))
            binding.rvChat.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun createAdapterModel(text: String, isBot: Boolean): ImageGenerationAdapterModel {
        return ImageGenerationAdapterModel(text,isBot)
    }
    private fun createRequest(query: String): ImageGenerationRequest {
        return  ImageGenerationRequest(
            n=1,
            prompt = query,
            size = "512x512"
        )
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}