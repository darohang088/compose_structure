package com.example.app.data.remote.dto

import com.example.app.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("company") val company: CompanyDto
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            companyName = company.name
        )
    }
}

data class CompanyDto(
    @SerializedName("name") val name: String
)
