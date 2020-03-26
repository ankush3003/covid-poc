package gulati.ankush.covidtracker.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    // lat, long, alt
    @JvmStatic
    @BindingAdapter("preText", "valTextDouble", requireAll = false)
    fun setTextViewData(view: TextView?, preText: String? = "Blank", valTextDouble: Double? = 0.0) {
        view?.let {it ->
            preText?.let {it1 ->
                if(valTextDouble != 0.0) {
                    val finalText = it1 + " " + valTextDouble.toString()
                    it.setText(finalText)
                } else {
                    it.setText(preText)
                }
            it.textSize = 18f
            }
        }
    }

    // lat, long, alt
    @JvmStatic
    @BindingAdapter("preText", "valTextFloat", requireAll = false)
    fun setTextViewDataFloat(view: TextView?, preText: String? = "Blank", valTextFloat: Float? = 0f) {
        view?.let {it ->
            preText?.let {it1 ->
                if(valTextFloat != 0f) {
                    val finalText = it1 + " " + valTextFloat.toString()
                    it.setText(finalText)
                } else {
                    it.setText(preText)
                }
            it.textSize = 18f
            }
        }
    }
    // lat, long, alt
    @JvmStatic
    @BindingAdapter("formatDateTime")
    fun setTextViewDate(view: TextView, dateInLong: Long?) {
        val calendar = Calendar.getInstance()
        val date: Date = Date()
        dateInLong?.let {
            calendar.setTimeInMillis(it)
            date.time = dateInLong
        } ?: return

        val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        val dateString = "Time: " + format.format(date)
        view.setText(dateString)
        view.textSize = 18f
    }
}