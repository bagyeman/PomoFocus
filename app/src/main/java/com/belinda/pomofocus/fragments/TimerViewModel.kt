package com.belinda.pomofocus.fragments

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Math.ceil
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    enum class ButtonState{
        Start, Pause, Resume
    }

    private lateinit var countDownTimer: CountDownTimer

    var buttonText = MutableLiveData("Start")

    var durationToMillis : Long = 0

    var focusTime = MutableLiveData("2:00")

    var breakTime = MutableLiveData("1:00")

    var counter = MutableLiveData(focusTime.value)

    private val _progress = MutableLiveData<Int>()
    val progress : LiveData<Int> = _progress

    private val _timerState = MutableLiveData(true)
    val timerState : LiveData<Boolean> = _timerState

//    var progressDouble : Double = 0.0
//
//    var progressValue : Double = 0.167

    var _isPaused = MutableLiveData(false)


    var timeLeft : Long = 0





//    private val _buttonState = MutableLiveData(true)
//    val buttonState : LiveData<Boolean> = _buttonState

    private val _buttonState = MutableLiveData(ButtonState.Start)
    val buttonState : LiveData<ButtonState> = _buttonState

    private val _done = MutableLiveData<Boolean>()
    val done : LiveData<Boolean> = _done


//    fun changeButtonState(){
//        //check current value of button and reverse it
//        _buttonState.value?.let { isTrue ->
//            //change button state so UI reacts to the changes
//            if (isTrue) {
//                _done.value =false
//                _buttonState.value = false
//                buttonText.value = "Pause"
//                startTimerCounter()
//            } else {
//                _done.value = true
//                countDownTimer.cancel()
//            }
//        }
//    }



    fun changeButtonState() {
        when (_buttonState.value) {
            ButtonState.Start -> {
                _buttonState.value = ButtonState.Pause
                _timerState.value?.let { isTrue ->
                     if (isTrue) {
                        timeLeft = TimeUnit.MINUTES.toMillis(2)
                         durationToMillis = timeLeft
                    } else {
                        timeLeft = TimeUnit.MINUTES.toMillis(1)
                         durationToMillis = timeLeft
                    }
                }
                startTimerCounter()
                buttonText.value = "Pause"
            }
            ButtonState.Pause -> {
                countDownTimer.cancel()
                _buttonState.value = ButtonState.Resume
                buttonText.value = "Resume"
            }
            ButtonState.Resume -> {
                startTimerCounter()
                _buttonState.value = ButtonState.Pause
                buttonText.value = "Pause"
            }
        }
    }

    fun startTimerCounter() {
        countDownTimer = object : CountDownTimer(timeLeft, TimeUnit.SECONDS.toMillis(1)){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                _progress.value = (kotlin.math.ceil((millisUntilFinished * 100.0) / durationToMillis)).toInt()
                val ms = String.format(
                        "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                )
                counter.value = ms
                // progressDouble = progressDouble.plus(progressValue)
//                _progress.value = progressDouble.toInt()


            }

            override fun onFinish() {
                _progress.value = 0
                if (timerState.value == true){
                    counter.value = breakTime.value
                    _timerState.value = false
                } else {
                    counter.value = focusTime.value
                    _timerState.value = true
                }
                _buttonState.value = ButtonState.Start
                buttonText.value = "Start"
                _done.value = true
            }

        }.start()
    }


    fun cancelTimer(){
        countDownTimer.cancel()
        _progress.value = 0
        _buttonState.value = ButtonState.Start
        if (_timerState.value == true) {
            counter.value = focusTime.value
            _timerState.value = false
        } else
            counter.value = breakTime.value
        _timerState.value = true
        buttonText.value = "Start"
    }





}


