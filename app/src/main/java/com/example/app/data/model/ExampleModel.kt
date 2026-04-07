package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class ExampleModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String?,
    @SerializedName("userId")
    val userId: Int?

    
)
