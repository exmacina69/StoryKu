package com.dicoding.storyku.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.storyku.data.response.ListStoryResponse

@Database(
    entities = [ListStoryResponse::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDataBase : RoomDatabase() {

    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDataBase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDataBase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}