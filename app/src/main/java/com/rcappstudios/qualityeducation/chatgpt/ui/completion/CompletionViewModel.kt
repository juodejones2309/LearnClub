package com.zero.chatgpt_androidapp.ui.completion

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import com.zero.chatgpt_androidapp.data.completion.model.response.CompletionResponse
import com.zero.chatgpt_androidapp.data.completion.repository.CompletionRemoteRepository
import com.zero.chatgpt_androidapp.networkutils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletionViewModel  @Inject constructor(
    private val app: Application,
    private val completionRemoteRepository: CompletionRemoteRepository
) : AndroidViewModel(app){

    val questionLiveData = SingleLiveEvent<CompletionRequest>()

    var completionResultLiveData = Transformations.switchMap(questionLiveData){
        completionRemoteRepository.getCompletionResult(it)
    }

}