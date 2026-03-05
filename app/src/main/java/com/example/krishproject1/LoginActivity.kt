package com.example.krishproject1


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit


@Composable
fun LogInScreen(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    val context = LocalContext.current
    // local data persistence for login info
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    var username by remember { mutableStateOf(prefs.getString("username", "") ?: "") }
    var password by remember { mutableStateOf(prefs.getString("password", "") ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // light purple background
            .background(Color(0xFFE1BEE7))
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            TextField(
                value = username,
                onValueChange = { newValue ->
                    username = newValue
                },
                label = { Text("Enter Username") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                },
                label = { Text("Enter Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {
                    prefs.edit { putString("username", username); putString("password", password) }
                    Toast.makeText(context, "You logged in", Toast.LENGTH_LONG).show()
                    // navigate to home screen
                    onLogin()
                },
                enabled =  checkFields(username, password)
            ) {
                Text("Login")
            }
            Image(
                painter = painterResource(id = R.drawable.adobe_express___file),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(500.dp)
                    .padding(16.dp)
            )
        }
    }
}

// Login req: checks username and password for length requirements
fun checkFields(username: String, password: String): Boolean{
    return username.length >=5 && password.length >=8 && !username.contains(" ") && !password.contains(" ")
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
   // LogInScreen()
}