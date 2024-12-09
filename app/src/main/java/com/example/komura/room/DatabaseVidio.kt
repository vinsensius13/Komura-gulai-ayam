package com.example.komura.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [VideoEntity::class, AudioEntity::class, ProfilePictureEntity::class], // Pastikan entitas benar
    version = 1,
    exportSchema = false
)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun audioDao(): AudioDao
    abstract fun profilePictureDao(): ProfilePictureDao

    companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getDatabase(context: Context): VideoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoDatabase::class.java,
                    "video_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
