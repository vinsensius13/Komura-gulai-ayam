package com.example.komura.AllPage.pagemain.setting

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.komura.R
import com.example.komura.room.VideoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSetting(
    finish: () -> Unit,
    navigateToChangePassword: () -> Unit,
    onLogout: () -> Unit,
    viewModel: VideoViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("MartinKelvin@gmail.com") }

    // Observasi foto profil dari Room melalui ViewModel
    val profilePictureUri by viewModel.profilePicture.observeAsState()
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Media picker untuk foto profil
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                photoUri = uri
                viewModel.saveProfilePicture(uri.toString()) // Simpan ke Room
                Log.d("PageSetting", "Selected photoUri: $uri")
            }
        }
    )

    // Sync photoUri dengan data dari Room
    LaunchedEffect(profilePictureUri) {
        photoUri = profilePictureUri?.uri?.let { Uri.parse(it) }
        Log.d("PageSetting", "Updated photoUri: $photoUri")
    }

    // Fetch nama dan email dari Firestore saat komposisi pertama kali dijalankan
    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    nama = document.getString("username") ?: "Username tidak ditemukan"
                    email = document.getString("email") ?: "Email tidak ditemukan"
                }
                .addOnFailureListener {
                    nama = "Gagal memuat username"
                    email = "Gagal memuat email"
                }
        } else {
            nama = "User belum login"
            email = "User belum login"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    MoreVertMenuExample(
                        navigateToChangePassword = navigateToChangePassword,
                        onLogout = onLogout
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(40.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Foto profil dengan ikon pensil
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = photoUri ?: R.drawable.martin3 // Default jika null
                        ),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .shadow(10.dp, CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Photo",
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") }
                            .padding(8.dp)
                    )
                }

                Text(
                    text = "Edit Profile",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)
                )

                Column {
                    Text("Your Name", Modifier.padding(top = 5.dp, bottom = 5.dp))
                    TextField(
                        value = nama,
                        onValueChange = { nama = it },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffe6e6e6)),
                        modifier = Modifier.width(400.dp),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Column {
                    Text("Your Email", Modifier.padding(top = 10.dp, bottom = 5.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffe6e6e6)),
                        modifier = Modifier.width(400.dp),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    onClick = {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val userMap = mapOf(
                                "username" to nama,
                                "email" to email
                            )
                            db.collection("users").document(userId)
                                .update(userMap)
                                .addOnSuccessListener {
                                    Log.d("PageSetting", "Profile updated successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("PageSetting", "Failed to update profile", e)
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff00a6ff))
                ) {
                    Text("Save")
                }
            }
        }
    )
}


@Composable
fun MoreVertMenuExample(
    navigateToChangePassword: () -> Unit,
    onLogout: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text("Change Password") },
            onClick = {
                expanded = false
                navigateToChangePassword()
            }
        )
        DropdownMenuItem(
            text = { Text("Logout") },
            onClick = {
                expanded = false
                onLogout()
            }
        )
    }
}
