package com.rcappstudios.qualityeducation.retrofit.api
import com.rcappstudio.placesapi.youtubeDataModel.YoutubeResults
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface Api {


    @GET("search")
    suspend fun getYoutubeResults(
        @Query("part") part : String,
        @Query("q") q: String,
        @Query("key") key : String,
        @Query("maxResults") maxResults: Int ?= 20
    ) : Response<YoutubeResults>

}