package com.rcappstudios.qualityeducation.fragments

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
import com.rcappstudios.qualityeducation.chatgpt.adapter.CompletionAdapter
import com.rcappstudios.qualityeducation.chatgpt.adapter.model.CompletionAdapterModel
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest
import com.rcappstudios.qualityeducation.databinding.FragmentChatGptBinding
import com.rcappstudios.qualityeducation.chatgpt.ui.completion.CompletionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatGptFragment : Fragment() {

    private lateinit var viewModel: CompletionViewModel
    private lateinit var adapter: CompletionAdapter
    private lateinit var binding: FragmentChatGptBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatGptBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CompletionViewModel::class.java]
        initAdapter()
        clickListener()
    }

    private fun initAdapter(){
        adapter = CompletionAdapter(requireContext(), mutableListOf())
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChat.adapter = adapter
        adapter.addCompletionData(createAdapterModel("Type any thing.....\uD83D\uDE03", true))
        attachObserver()
    }

    private fun clickListener(){
        binding.btnAsk.setOnClickListener {
            if(binding.etQuestion.text.toString() != ""){
                viewModel.questionLiveData.value = createRequest(binding.etQuestion.text.toString())
                adapter.addCompletionData(createAdapterModel(binding.etQuestion.text.toString(),false))
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
        viewModel.completionResultLiveData.observe(viewLifecycleOwner){
            Log.d("TAGData", "attachObserver: $it")
            adapter.addCompletionData(createAdapterModel(it?.choices?.get(0)!!.text.toString(),true))
            binding.rvChat.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun createAdapterModel(text: String, isBot: Boolean): CompletionAdapterModel {
        return CompletionAdapterModel(text,isBot)
    }
    private fun createRequest(query: String): CompletionRequest {
        return  CompletionRequest(
            model = "text-davinci-003",
            prompt = query,
            maxTokens = 100,
            temperature= 0,
            stream =  false
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