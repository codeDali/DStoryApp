package com.dicoding.picodiploma.loginwithanimation.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem

@Dao
interface StoriesDao {
    @Query("SELECT * FROM stories")
    fun getAllStories() : PagingSource<Int, StoriesItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<StoriesItem>)

    @Query("DELETE FROM stories")
    suspend fun clearAll()
}