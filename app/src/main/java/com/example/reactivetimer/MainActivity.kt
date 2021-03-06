package com.example.reactivetimer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.observable.ObservableCreate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var minutes: Int = 0
    var seconds: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPickerMinutes.minValue = 0
        numberPickerMinutes.maxValue = 60
        numberPickerMinutes.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
                if (picker!!.value < 10) {
                    tvMin.text = "0${picker.value}"
                } else {
                    tvMin.text = picker.value.toString()
                }
                Log.d("MyLOG", picker.toString())
                minutes = picker!!.value
            }
        })
        numberPickerSeconds.minValue = 0
        numberPickerSeconds.maxValue = 60
        numberPickerSeconds.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
                if (picker!!.value < 10) {//
                    tvSec.text = "0${picker.value}"
                } else {
                    tvSec.text = picker.value.toString()
                }
//                tvSec.text = String.format(newVal.toString())

                Log.d("MyLOG", picker.toString())
                seconds = picker.value
            }
        })


        btStart.setOnClickListener {
            numberPickerSeconds.value = 0
            numberPickerMinutes.value = 0
            btStart.isEnabled = false
            btStart.text = ""
            numberPickerMinutes.isEnabled = false
            numberPickerSeconds.isEnabled = false

            val dispose = getSeconds().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe() {
                    val min = it / 60
                    val sec = it % 60
                    if (min < 10) {
                        tvMin.text = "0" + min
                        if (sec < 10) {
                            tvSec.text = "0" + sec
                        } else {
                            tvSec.text = "" + sec
                        }
                    } else {
                        tvMin.text = "" + min
                        if (sec < 10) {
                            tvSec.text = "0" + sec
                        } else {
                            tvSec.text = "" + sec
                        }
                    }
                    if (tvSec.text.equals("00") && tvMin.text.equals("00")) {
                        btStart.isEnabled = true
                        btStart.text = "Start"
                        numberPickerMinutes.isEnabled = true
                        numberPickerSeconds.isEnabled = true
                        getMusic()
                        getVibro()

                    }
                }
        }
    }

    fun getSeconds(): Observable<Int> {
        return ObservableCreate { subcrumber ->
            val count = minutes * 60 + seconds
            for (i in count downTo 0) {
                SystemClock.sleep(1000)
                subcrumber.onNext(i)
            }
        }
    }
    fun getMusic(){
        val mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        mp.start();
    }
    fun getVibro(){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val milliseconds = 1000L
        val canVibrate: Boolean = vibrator.hasVibrator()
        vibrator.vibrate(milliseconds)
    }
}