package com.dms.pmsandroid.feature.notify

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

object NotifyBindingAdapter {
    @JvmStatic
    @BindingAdapter("showTimeAdapter")
    fun timeAdapter(textView: TextView, time:Date?){
        if(time!=null){
            val dateFormat=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz", Locale.KOREA)
            val currentDateTime= System.currentTimeMillis()
            val date=Date(currentDateTime)
            val currentTime=dateFormat.format(date)
            val getTime=dateFormat.format(time)
            val longCurrentTime=dateFormat.parse(currentTime).time
            val longGetTime=dateFormat.parse(getTime).time
            val diff=(longCurrentTime-longGetTime)/1000
            val dayDiff=(diff/86400)
            if(dayDiff<0||dayDiff>=31){
                val dateFormat= SimpleDateFormat("yyyy-MM-dd",Locale.KOREA)
                textView.text=dateFormat.format(time)
            }else{
                if(dayDiff<=0){
                    when(diff) {
                        in 0..60->
                            textView.text = "방금"
                        in 61..120->textView.text="1분전"
                        in 121..3600->
                            textView.text="${diff/60}분 전"
                        in 3601..7200->textView.text="1시간 전"
                        else ->textView.text="${diff/3600}시간 전"
                    }
                }else{
                    when(dayDiff){
                        1.toLong() ->textView.text="어제"
                        in 2..6->textView.text="${dayDiff}일 전"
                        else -> textView.text="${dayDiff/7}주 전"
                    }
                }
            }
        }

    }
}