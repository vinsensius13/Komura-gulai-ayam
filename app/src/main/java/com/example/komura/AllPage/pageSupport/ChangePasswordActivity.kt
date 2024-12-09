package com.example.komura.AllPage.pageSupport

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.komura.AllPage.pagemain.setting.ChangePasswordScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider

class ChangePasswordActivity : ComponentActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangePasswordScreen(
                finish = { finish() },
                onPasswordChange = { oldPassword, newPassword ->
                    changePassword(oldPassword, newPassword)
                }
            )
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            // Reauthenticate user with old password
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // Update password
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                            finish() // Kembali ke layar sebelumnya
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal mengubah password: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Password lama salah: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User tidak valid", Toast.LENGTH_SHORT).show()
        }
    }
}
