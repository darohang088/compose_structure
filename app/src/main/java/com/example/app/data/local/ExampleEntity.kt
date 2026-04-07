package com.example.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.app.data.model.ExampleModel

@Entity(tableName = "example_table")
data class ExampleEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String?,
    val userId: Int?
)

// Extension function to map Entity to Model
fun ExampleEntity.toModel(): ExampleModel {
    return ExampleModel(
        id = id,
        title = title,
        body = body,
        userId = userId
    )
}

// Extension function to map Model to Entity
fun ExampleModel.toEntity(): ExampleEntity {
    return ExampleEntity(
        id = id,
        title = title,
        body = body,
        userId = userId
    )
}
