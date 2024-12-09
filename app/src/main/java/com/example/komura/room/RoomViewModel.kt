package com.example.komura.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val videoDao = VideoDatabase.getDatabase(application).videoDao()
    private val audioDao = VideoDatabase.getDatabase(application).audioDao()
    private val profilePictureDao = VideoDatabase.getDatabase(application).profilePictureDao()

    val allVideos: LiveData<List<VideoEntity>> = videoDao.getAllVideos()
    val allAudio: LiveData<List<AudioEntity>> = audioDao.getAllAudio()
    val profilePicture: LiveData<ProfilePictureEntity?> = profilePictureDao.getProfilePicture()

    fun insertAudio(audio: AudioEntity) {
        viewModelScope.launch {
            audioDao.insert(audio)
        }
    }

    fun insertVideo(video: VideoEntity) {
        viewModelScope.launch {
            videoDao.insert(video)
        }
    }

    fun saveProfilePicture(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uri.isNotBlank()) {
                profilePictureDao.insertOrUpdate(ProfilePictureEntity(id = 1, uri = uri))
            }
        }
    }
}
