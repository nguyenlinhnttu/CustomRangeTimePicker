package layout

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.android.customrangetimepicker.R
import com.android.customrangetimepicker.RangePickerListener
import kotlinx.android.synthetic.main.layout_range_time_picker.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by NguyenLinh on 17,November,2020
val picker = PickerRangeTimeDialog.newInstance(null, null)
picker.listener = object : RangePickerListener {
override fun onTimePick(startTime: String?, endTime: String?) {
startTime?.let {
lnStartTime.setRightText(startTime)
endTime?.let {
lnStartTime.setRightText(lnStartTime.getRightText() + "～" + endTime)
}
}
}
}
picker.show(childFragmentManager, "PickerRangeTimeDialog")
 */
class PickerRangeTimeDialog : DialogFragment() {
    //Start time- End time arguments
    private var startTime: String? = null
    private var endTime: String? = null

    //Callback return time selected
    var listener: RangePickerListener? = null

    //Mode picker start/end
    private var isStartMode = true

    //Format date/time
    private val fullSDF = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private val timeSDF = SimpleDateFormat("HH:mm")
    companion object {

        private const val KEY_START_TIME = "KEY_START_TIME" //HH:mm
        private const val KEY_END_TIME = "KEY_END_TIME"  //HH:mm
        fun newInstance(startTime: String?, endTime: String?): PickerRangeTimeDialog {
            val args = Bundle()
            args.putString(KEY_START_TIME, startTime)
            args.putString(KEY_END_TIME, endTime)
            val fragment = PickerRangeTimeDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_range_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTime = arguments?.getString(KEY_START_TIME)
        endTime = arguments?.getString(KEY_END_TIME)
        timePicker.setIs24HourView(true)
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val date = fullSDF.parse("$currentDate $hourOfDay:$minute")
            date?.let {
                if (isStartMode) {
                    startTime = timeSDF.format(date)
                    Log.d("Picker", startTime!!)
                } else {
                    endTime = timeSDF.format(date)
                    Log.d("Picker", endTime!!)
                }
            }

        }
        setUpStartTime()

        tvOK.setOnClickListener {
            startTime?.let { start ->
                endTime?.let { end ->
                    //Full DateTime: yyyy-MM-dd HH:mm
                    val startFull = "$currentDate $startTime"
                    val endFull = "$currentDate $endTime"
                    //Convert to Date
                    val dateStart: Date = parserDate(startFull)
                    val dateEnd: Date = parserDate(endFull)
                    //Compare start with end
                    if (dateStart.time > dateEnd.time) {
                        //TODO: Show Alert Error nếu cần.
                    } else {
                        Log.d("Picker", "$start～$end")
                        listener?.onTimePick(start, end)
                        dialog?.dismiss()
                    }
                }.run {
                    listener?.onTimePick(startTime, endTime)
                    dialog?.dismiss()
                }
            }
        }
        btnStartTime.setOnClickListener {
            setUpStartTime()
        }
        btnEndTime.setOnClickListener {
            setUpEndTime()
        }
    }

    private fun setUpStartTime() {
        context?.let {
            btnStartTime.setTextColor(ContextCompat.getColor(it, R.color.goldenYellow))
            btnEndTime.setTextColor(ContextCompat.getColor(it, R.color.white))
            btnEndTime.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_end_time_24dp, 0, 0)
            btnStartTime.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_start_time_selected_24dp,
                0,
                0
            )
        }
        isStartMode = true
        startTime?.let {
            val startFull = "$currentDate $startTime"
            val cal = Calendar.getInstance()
            cal.time = parserDate(startFull)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
                timePicker.minute = cal.get(Calendar.MINUTE)
            } else {
                timePicker.currentHour = cal.get(Calendar.HOUR_OF_DAY)
                timePicker.currentMinute = cal.get(Calendar.MINUTE)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                startTime = "${timePicker.hour}:${timePicker.minute}"
            } else {
                startTime = "${timePicker.currentHour}:${timePicker.currentMinute}"
            }
            Log.d("PickerStartTime", it)
        }
    }

    //Format full: yyyy-MM-dd HH:mm to Date
    private fun parserDate(fullDate: String): Date {
        val date = fullSDF.parse(fullDate)
        date?.let {
            return it
        }.run {
            return Date()
        }
    }

    private fun setUpEndTime() {
        context?.let {
            btnEndTime.setTextColor(ContextCompat.getColor(it, R.color.goldenYellow))
            btnStartTime.setTextColor(ContextCompat.getColor(it, R.color.white))
            btnStartTime.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_start_time_24dp,
                0,
                0
            )
            btnEndTime.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_end_time_selected_24dp,
                0,
                0
            )
        }
        isStartMode = false
        endTime?.let {
            val endFull = "$currentDate $endTime"
            val cal = Calendar.getInstance()
            cal.time = parserDate(endFull)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
                timePicker.minute = cal.get(Calendar.MINUTE)
            } else {
                timePicker.currentHour = cal.get(Calendar.HOUR_OF_DAY)
                timePicker.currentMinute = cal.get(Calendar.MINUTE)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                endTime = "${timePicker.hour}:${timePicker.minute}"
            } else {
                endTime = "${timePicker.currentHour}:${timePicker.currentMinute}"
            }
            Log.d("PickerEndTime", it)
        }

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}