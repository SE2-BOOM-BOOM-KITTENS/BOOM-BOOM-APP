package com.example.boomboomfrontend.game

import android.content.Context
import android.view.animation.AnimationUtils
import android.media.MediaPlayer
import android.view.View
import com.example.boomboomfrontend.R

class ShuffleActionHandler (private val context: Context, private val gameClient: GameClient, private val shuffleView: View) {

    private var shakeDetector: ShakeDetector? = null

    fun startListeningForShake(){
        shakeDetector = ShakeDetector(context){
            shuffleDeck() // wenn Schütteln erkannt wird, deck shufflen
        }
    }

    private fun shuffleDeck(){
        // Kommunikation mit dem Server
        gameClient.shuffleDeck()

        // visuelle Rückmeldung
        showShuffleAnimation()
        //playShuffleSound()
    }

    private fun showShuffleAnimation(){
        val shuffleAnimation = AnimationUtils.loadAnimation(context, R.anim.shuffle_animation)
        shuffleView.startAnimation(shuffleAnimation)
    }

    /*
    private fun playShuffleSound(){
        val sound = MediaPlayer.create(context, R.raw.shuffle_sound)
        sound.start()
    }*/

}