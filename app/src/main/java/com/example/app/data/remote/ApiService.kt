package com.example.app.data.remote

import com.example.app.data.model.ExampleModel
import com.example.app.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.ENDPOINT_PATH)
    suspend fun getExamples(): Response<List<ExampleModel>>
}
