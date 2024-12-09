package com.example.komura.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_table")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val filePath: String,
    val dateTime: String
)

@Entity(tableName = "audio_table")
data class AudioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val filePath: String,
    val dateTime: String
)

@Entity(tableName = "profile_picture_table")
data class ProfilePictureEntity(
    @PrimaryKey val id: Int = 0,
    val uri: String
)

