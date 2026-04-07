package com.example.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExampleDao {
    @Query("SELECT * FROM example_table")
    suspend fun getAllExamples(): List<ExampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamples(examples: List<ExampleEntity>)

    @Query("DELETE FROM example_table")
    suspend fun clearExamples()
}
