package com.example.reactivetimer

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
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
        numberPickerMinutes.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
               tvMin.text = String.format(newVal.toString())
                Log.d("MyLOG",picker.toString())
                minutes = picker!!.value
            }
        })
        numberPickerSeconds.minValue = 0
        numberPickerSeconds.maxValue = 60
        numberPickerSeconds.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
                tvSec.text = String.format(newVal.toString())
                Log.d("MyLOG",picker.toString())
                seconds = picker!!.value
            }
        })


        btStart.setOnClickListener {

            val dispose = getSeconds().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe() {
                    val min = it / 60
                    tvMin.text = min.toString()
                    tvSec.text = (it % 60).toString()
                }
        }
    }

    fun getSeconds(): Observable<Int> {
        return ObservableCreate { subcrumber ->
            for (i in minutes downTo 0) {
                SystemClock.sleep(1000)
                subcrumber.onNext(i)
            }
        }

    }


}