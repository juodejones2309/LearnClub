package com.rcappstudios.qualityeducation.chatgpt.ui.completion

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.rcappstudios.qualityeducation.chatgpt.completion.model.request.CompletionRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.model.repository.CompletionRemoteRepository
import com.rcappstudios.qualityeducation.chatgpt.networkutils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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