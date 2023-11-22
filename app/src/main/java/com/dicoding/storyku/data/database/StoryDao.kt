package com.dicoding.storyku.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.storyku.data.response.ListStoryResponse

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryResponse>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryResponse>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}