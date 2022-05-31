package com.example.popit

object GameController {

    private val gameState = GameState()

    private var gameEndRunnable: Runnable? = null
    private var isGameEnded = false

    fun userScore() = gameState.bubblePoppedScore()

    fun start(runnable: Runnable) {
        isGameEnded = false
        gameEndRunnable = runnable

        gameState.start()
    }

    fun bubbleDestroyed() {
        gameState.bubbleDestroyed()

        checkIfGameEnded()
    }

    fun bubblePopped() {
        gameState.bubblePopped()
    }

    private fun checkIfGameEnded() {
        if (isGameEnded) {
            return
        }

        if (gameState.bubbleDestroyedScore() > gameState.bubblePoppedScore()) {
            gameEndRunnable?.run()
            isGameEnded = true
        }
    }

}