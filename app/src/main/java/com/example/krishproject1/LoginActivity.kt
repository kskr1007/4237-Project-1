package com.example.krishproject1


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent


@Composable
fun LogInScreen(modifier: Modifier = Modifier) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // for toast messages
    val context = LocalContext.current

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
                Toast.makeText(context, "You logged in", Toast.LENGTH_LONG).show()
                //using intent to move to the HomeScreen once logged in
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }, enabled = checkUsernameAndPassword(username,password) && checkLengthsAndSpaces(username, password)
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

fun checkUsernameAndPassword(username: String, password: String): Boolean {
    return username.isNotBlank() && password.isNotBlank()
}
fun checkLengthsAndSpaces(username: String, password: String): Boolean{
    return username.length >=5 && password.length >=8 && !username.contains(" ") && !password.contains(" ")
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LogInScreen()
}