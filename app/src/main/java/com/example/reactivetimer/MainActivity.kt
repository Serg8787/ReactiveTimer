package com.example.reactivetimer

import android.os.Bundle
import android.os.SystemClock
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
//               tvMin.text = String.format(newVal.toString())
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
            numberPickerMinutes.isEnabled = false
            numberPickerSeconds.isEnabled = false

            val dispose = getSeconds().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe() {
                    if ((it % 60) < 10) {
                        tvSec.text = "0" + (it % 60).toString()
                        tvMin.text = "0" + (it / 60)
                    } else {
                        tvSec.text = (it % 60).toString()
                        tvMin.text = "0"+(it / 60).toString()
                    }


                    if (tvSec.text.equals("00")) {
                        btStart.isEnabled = true
                        numberPickerMinutes.isEnabled = true
                        numberPickerSeconds.isEnabled = true
                    }
                }.isDisposed
        }
    }

    fun getSeconds(): Observable<Int> {
        return ObservableCreate { subcrumber ->
            val count = minutes * 60 + seconds
            for (i in count downTo 0) {
                SystemClock.sleep(1000)
                subcrumber.onNext(i)
            }
            subcrumber.onComplete()

        }
    }

}