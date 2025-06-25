package com.example.boomboomfrontend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.boomboomfrontend.R
import com.example.boomboomfrontend.ui.gameUI.background
import com.example.boomboomfrontend.ui.gameUI.border
import com.example.boomboomfrontend.ui.gameUI.cardback
import com.example.boomboomfrontend.viewmodel.RegisterViewModel

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
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
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(background))
    ){
        Image(
            painter = painterResource(id = R.drawable.gradientbackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.CenterStart
        ){
            Column {
                TextField(
                    value = registerViewModel.name,
                    onValueChange = { registerViewModel.onTextChanged(it) },
                    label = { Text("Enter your name") })

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = {
                    registerViewModel.onButtonPress()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(cardback)
                    ),
                    modifier = Modifier.size(120.dp, 40.dp)
                        .border(2.dp, Color(border), RoundedCornerShape(10.dp))
                        .background(Color(cardback), RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (errorMessage != null){
                    Text(text=errorMessage, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}