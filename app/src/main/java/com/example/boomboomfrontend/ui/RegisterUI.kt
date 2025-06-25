package com.example.boomboomfrontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boomboomfrontend.viewmodel.RegisterViewModel

@Preview
@Composable
fun RegisterScreenPreview(navController: NavController = rememberNavController()){
    RegisterScreen(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()){

    val isRegistered = registerViewModel.isRegistered
    val errorMessage = registerViewModel.errorMessage

    if(isRegistered){
        LaunchedEffect(Unit) {
            navController.navigate("connection-screen"){
                popUpTo("register"){inclusive=true}
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = registerViewModel.name,
            onValueChange = { registerViewModel.onTextChanged(it) },
            label = { Text("Enter your name") })

        Button(onClick = {
            registerViewModel.onButtonPress()
        }) {
            Text("Register")
        }

        Text("Click Bypass if you don't want to register and connect to server. TEMPORARY; DELETE LATER")
        Button(onClick = {
            navController.navigate("connection-screen")
        }) {
            Text("Bypass")
        }

        if (errorMessage != null){
            Text(text=errorMessage, modifier = Modifier.padding(top = 8.dp))
        }
    }
}