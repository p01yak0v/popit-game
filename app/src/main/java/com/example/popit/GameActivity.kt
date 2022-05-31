package com.example.popit

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import java.util.*

private const val BUBBLE_SIZE = 50
private const val BUBBLE_SIZE_MAX = 500
private const val RUN_GAME_LOOP_DURATION = 500L
private const val ANIMATION_DURATION = 3000L

class GameActivity : AppCompatActivity() {
    private lateinit var gameContainer: FrameLayout
    private val handler = Handler(Looper.getMainLooper())

    private val bubbleColors = arrayOf(R.color.purple_200, R.color.purple_500, R.color.purple_700, R.color.black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameContainer = findViewById(R.id.gameContainer)

        GameController.start {
            showEndGameDialog()
            stopGame()
        }

        gameContainer.post {
            runGameLoop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGame()
    }

    private fun stopGame() {
        handler.removeCallbacksAndMessages(null)
        gameContainer.removeAllViews()
    }

    private fun showEndGameDialog() {
        AlertDialog.Builder(this)
            .setMessage("Игра окончена. Ваш результат: ${GameController.userScore()}")
            .setPositiveButton("ШТОШ") { _, _ -> finish() }
            .create()
            .show()
    }

    private fun runGameLoop() {
        spawnBubble()

        handler.postDelayed(
            { runGameLoop() },
            RUN_GAME_LOOP_DURATION
        )
    }

    private fun spawnBubble() {
        val bubbleView = View(this)

        bubbleView.layoutParams = createLayoutParams()
        bubbleView.setBackgroundResource(R.drawable.shape_bubble)

        setRandomColor(bubbleView)

        val animator = createAnimation(bubbleView)
        animator.start()

        bubbleView.setOnClickListener {
            GameController.bubblePopped()
            gameContainer.removeView(it)
            animator.cancel()
        }

        gameContainer.addView(bubbleView)
    }

    private fun setRandomColor(bubbleView: View) {
        val random = Random()
        val color = bubbleColors[random.nextInt(bubbleColors.size)]

        val bg = bubbleView.background as GradientDrawable
        bg.setColor(ContextCompat.getColor(this, color))
    }

    private fun createLayoutParams(): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(BUBBLE_SIZE, BUBBLE_SIZE)

        val random = Random()

        params.topMargin = adjustMargin(
            random.nextInt(gameContainer.height),
            gameContainer.height
        )

        params.leftMargin = adjustMargin(
            random.nextInt(gameContainer.width),
            gameContainer.width
        )

        return params
    }

    private fun adjustMargin(margin: Int, upperBound: Int): Int {
        return if (margin + BUBBLE_SIZE >= upperBound) {
            margin - BUBBLE_SIZE
        } else {
            margin
        }
    }

    private fun createAnimation(bubbleView: View): Animator {
        return ValueAnimator.ofInt(BUBBLE_SIZE, BUBBLE_SIZE_MAX).apply {
            duration = ANIMATION_DURATION

            addUpdateListener { animator ->
                val newSize = animator.animatedValue as Int
                bubbleView.updateLayoutParams<FrameLayout.LayoutParams> {

                    val deltaX = (newSize - width) / 2
                    val deltaY = (newSize - height) / 2

                    bubbleView.translationX -= deltaX
                    bubbleView.translationY -= deltaY

                    width = newSize
                    height = newSize
                }
            }

            addListener(
                onEnd = {
                    if (bubbleView.isShown) {
                        gameContainer.removeView(bubbleView)
                        GameController.bubbleDestroyed()
                    }
                }
            )
        }
    }
}