package com.example.boomboomfrontend.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boomboomfrontend.network.ApiRepository
import com.example.boomboomfrontend.viewmodel.gameState.ClientInfoHolder
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterViewModel : ViewModel() {
    private val clientInfo = ClientInfoHolder.clientInfo
    private val api = ApiRepository()
    var name by mutableStateOf("")
    var isRegistered by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    fun onTextChanged(newText: String){
        name = newText
    }

    fun onButtonPress(){
        clientInfo.playerName = name
        Log.i("REGISTER", "${clientInfo.playerName} trying to register")
        sendRequest();
    }

    private fun sendRequest(){
        viewModelScope.launch {
            try{
                val response = api.addPlayer(name)
                if (response.isSuccessful){
                    val responseBody = response.body()?.string()
                    isRegistered = true
                    errorMessage = null
                    Log.i("REGISTER","Registration successful")
                    clientInfo.playerId = UUID.fromString(responseBody)
                    Log.i("REGISTER", "Id is ${clientInfo.playerId}")
                } else {
                    isRegistered = false
                    errorMessage = "Registration failed: ${response.code()}"
                }
            } catch (e:Exception){
                isRegistered = false
                errorMessage = "Network error: ${e.localizedMessage}"
            }
        }
    }

}