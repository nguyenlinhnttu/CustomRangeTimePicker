package com.android.customrangetimepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import layout.PickerRangeTimeDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val picker = PickerRangeTimeDialog.newInstance("10:00", "14:00")
        picker.listener = object : RangePickerListener {
            override fun onTimePick(startTime: String?, endTime: String?) {

            }
        }
        picker.show(supportFragmentManager, "PickerRangeTimeDialog")
    }
}