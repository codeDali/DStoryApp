package com.dicoding.picodiploma.loginwithanimation.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem

@Database(entities = [StoriesItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
) abstract class StoriesDatabase: RoomDatabase() {

    abstract fun storyDao(): StoriesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE : StoriesDatabase? = null
        fun getDatabase(context: Context) : StoriesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                StoriesDatabase::class.java,
                "story_database"
            ).build().also { INSTANCE = it }
        }
    }
}