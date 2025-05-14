package com.example.boomboomfrontend.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DeckViewModel : ViewModel() {

    // speichert Zustand des Decks
    private val _deckState = MutableLiveData<List<Card>>()

    val deckState: LiveData<List<Card>> get() = _deckState

    fun updateDeckState(newDeckState: List<Card>){
        _deckState.value = newDeckState
    }
}