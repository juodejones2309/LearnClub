package com.zero.chatgpt_androidapp.ui.completion

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcappstudios.qualityeducation.databinding.FragmentCompletionBinding
import com.zero.chatgpt_androidapp.adapter.CompletionAdapter
import com.zero.chatgpt_androidapp.adapter.model.CompletionAdapterModel
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CompletionFragment : Fragment() {

    private lateinit var viewModel: CompletionViewModel
    private lateinit var adapter: CompletionAdapter
    private lateinit var binding: FragmentCompletionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletionBinding.inflate(layoutInflater,container,false)
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
    private fun createRequest(query: String): CompletionRequest{
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