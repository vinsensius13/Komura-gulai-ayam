package com.example.komura.AllPage.pagemain.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    finish: () -> Unit,
    onPasswordChange: (String, String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = finish) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Change Your Password",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                CustomTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = "Old Password",
                    showPassword = showOldPassword,
                    onShowPasswordToggle = { showOldPassword = !showOldPassword }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password",
                    showPassword = showNewPassword,
                    onShowPasswordToggle = { showNewPassword = !showNewPassword }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    showPassword = showConfirmPassword,
                    onShowPasswordToggle = { showConfirmPassword = !showConfirmPassword }
                )
                Spacer(modifier = Modifier.height(32.dp))
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                Button(
                    onClick = {
                        if (newPassword == confirmPassword) {
                            if (oldPassword.isNotBlank() && newPassword.isNotBlank()) {
                                onPasswordChange(oldPassword, newPassword)
                                errorMessage = ""
                            } else {
                                errorMessage = "All fields must be filled"
                            }
                        } else {
                            errorMessage = "New Password and Confirm Password do not match"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff00a6ff), // Warna biru tombol
                        contentColor = Color.White // Warna teks tombol
                    ),
                    shape = RoundedCornerShape(10.dp) // Rounded penuh
                ) {
                    Text("Change Password", fontSize = 16.sp)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onShowPasswordToggle: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onShowPasswordToggle) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xffe6e6e6), // Warna latar abu-abu
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black, // Warna teks saat fokus
            unfocusedTextColor = Color.Black, // Warna teks saat tidak fokus
            cursorColor = Color.Black // Warna kursor
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Tinggi disesuaikan untuk keseragaman
    )
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(
        finish = {},
        onPasswordChange = { oldPassword, newPassword ->
            // Contoh callback untuk simulasi
        }
    )
}