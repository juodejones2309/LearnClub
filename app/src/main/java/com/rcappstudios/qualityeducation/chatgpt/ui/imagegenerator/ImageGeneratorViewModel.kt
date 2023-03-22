package com.rcappstudios.qualityeducation.chatgpt.ui.imagegenerator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.rcappstudios.qualityeducation.chatgpt.completion.model.image.request.ImageGenerationRequest
import com.rcappstudios.qualityeducation.chatgpt.completion.repository.CompletionRemoteRepository
import com.rcappstudios.qualityeducation.chatgpt.networkutils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGeneratorViewModel @Inject constructor(
    private val app: Application,
    private val completionRemoteRepository: CompletionRemoteRepository
) : AndroidViewModel(app) {

    val imageGenerateRequestLiveData = SingleLiveEvent<ImageGenerationRequest>()

    var imageGenerationResult = Transformations.switchMap(imageGenerateRequestLiveData) {
        completionRemoteRepository.getImageGenerationResult(it)
    }

}