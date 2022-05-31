package com.example.popit

const val DESTROYED_SCORE = 20
const val POPPED_SCORE = 10

class GameState {

    var bubblesDestroyedCount: Int = 0
        private set

    var bubblesPoppedCount: Int = 0
        private set

    fun bubbleDestroyed() {
        bubblesDestroyedCount++
    }

    fun bubblePopped() {
        bubblesPoppedCount++
    }

    fun start() {
        bubblesDestroyedCount = 0
        bubblesPoppedCount = 0
    }

    fun bubbleDestroyedScore() = bubblesDestroyedCount * DESTROYED_SCORE

    fun bubblePoppedScore() = bubblesPoppedCount * POPPED_SCORE
}