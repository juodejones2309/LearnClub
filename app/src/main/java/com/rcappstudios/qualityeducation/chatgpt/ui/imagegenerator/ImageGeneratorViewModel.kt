package com.zero.chatgpt_androidapp.ui.imagegenerator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.zero.chatgpt_androidapp.data.completion.model.image.request.ImageGenerationRequest
import com.zero.chatgpt_androidapp.data.completion.model.request.CompletionRequest
import com.zero.chatgpt_androidapp.data.completion.repository.CompletionRemoteRepository
import com.zero.chatgpt_androidapp.networkutils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGeneratorViewModel  @Inject constructor(
    private val app: Application,
    private val completionRemoteRepository: CompletionRemoteRepository
) : AndroidViewModel(app){

    val imageGenerateRequestLiveData = SingleLiveEvent<ImageGenerationRequest>()

    var imageGenerationResult = Transformations.switchMap(imageGenerateRequestLiveData){
        completionRemoteRepository.getImageGenerationResult(it)
    }

}