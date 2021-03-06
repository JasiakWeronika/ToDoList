package com.example.game

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Hangman : AppCompatActivity() {
    private var images: Array<ImageView?>? = null
    private var numberParts = 6
    private var currentPart = 0
    private var numberOfChars = 0
    private var numberCorrect = 0
    private var words: Array<String> = emptyArray()
    private var currentWord: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hangman)
        var resources = resources
        words = resources.getStringArray(R.array.words)
        currentWord = ""
        images?.set(0, findViewById(R.id.head))
        images?.set(1, findViewById(R.id.body))
        images!![2] = findViewById(R.id.arm1)
        images!![3] = findViewById(R.id.arm2)
        images!![4] = findViewById(R.id.leg1)
        images!![5] = findViewById(R.id.leg2)
        playGame()
    }

    fun playGame() {
        currentPart = 0
        numberOfChars = currentWord.length
        numberCorrect = 0

        for (i in 1.. numberParts) {
            images?.get(i)?.visibility = View.INVISIBLE
        }
    }

    fun buttonPressed(view: View) {
        val letter = (view as TextView).text.toString()
        val letterChar = letter.get(0)
        view.isEnabled = false
        var correct = false

    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             