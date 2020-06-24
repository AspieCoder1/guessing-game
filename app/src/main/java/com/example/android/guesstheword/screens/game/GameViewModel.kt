package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    private val timer: CountDownTimer;

    // The current word
    private val _word: MutableLiveData<String> = MutableLiveData()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Current time
    private val _time = MutableLiveData<Long>()
    val timeLeft: LiveData<Long>
        get() = _time

    val currentTimeString: LiveData<String> = Transformations.map(_time) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // Game finished
    private val _finished: MutableLiveData<Boolean> = MutableLiveData()
    val finished: LiveData<Boolean>
        get() = _finished

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _time.value = DONE
                _finished.value = true
            }

            override fun onTick(timeRemaining: Long) {
                _time.value = (timeRemaining / ONE_SECOND)
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = (_score.value)?.dec()
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.inc()
        nextWord()
    }

    fun onGameFinishCompleted() {
        _finished.value = false
    }
}