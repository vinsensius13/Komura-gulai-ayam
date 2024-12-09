package com.example.komura.AllPage.pageSupport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.komura.AllPage.pagemain.setting.PageSetting
import com.example.komura.AllPage.loginDanRegistrasi.NavigasiActivityLoginAndRegistrasi
import com.example.komura.room.VideoViewModel
import com.google.firebase.auth.FirebaseAuth

class ActivityAccount : ComponentActivity() {
    // Gunakan VideoViewModel untuk mengelola semua data
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityAccount", "onCreate called") // Log lifecycle onCreate
        enableEdgeToEdge() // Mengaktifkan edge-to-edge untuk tampilan penuh

        setContent {
            PageSetting(
                finish = {
                    Log.d("ActivityAccount", "Activity finished") // Log saat finish() dipanggil
                    finish()
                },
                navigateToChangePassword = {
                    Log.d("ActivityAccount", "Navigating to ChangePasswordActivity") // Log navigasi
                    val intent = Intent(this, ChangePasswordActivity::class.java)
                    startActivity(intent)
                },
                onLogout = {
                    Log.d("ActivityAccount", "Logout initiated") // Log proses logout
                    FirebaseAuth.getInstance().signOut() // Proses logout pengguna
                    Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()

                    // Navigasi ke halaman login
                    val loginIntent = Intent(this, NavigasiActivityLoginAndRegistrasi::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(loginIntent)
                    Log.d("ActivityAccount", "Navigating to NavigasiActivityLoginAndRegistrasi")
                    finish()
                },
                viewModel = videoViewModel // Menghubungkan ViewModel ke composable
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityAccount", "onStart called") // Log lifecycle onStart
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityAccount", "onResume called") // Log lifecycle onResume
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityAccount", "onPause called") // Log lifecycle onPause
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityAccount", "onStop called") // Log lifecycle onStop
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityAccount", "onDestroy called") // Log lifecycle onDestroy
    }
}
