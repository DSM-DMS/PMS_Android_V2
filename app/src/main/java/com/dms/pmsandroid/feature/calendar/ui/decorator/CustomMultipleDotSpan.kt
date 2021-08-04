package com.dms.pmsandroid.feature.calendar.ui.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.dms.pmsandroid.R

class CustomMultipleDotSpan(private val dots:Int,context: Context) : LineBackgroundSpan {

    private val radius = 7F
    private val dotColor = arrayListOf(
        context.resources.getColor(R.color.green),
        context.resources.getColor(R.color.red),
        Color.BLUE
    )

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        val total = if(dots>3) 3 else dots
        var leftMost = (total - 1) * -12

        for(i in 0 until total){
            val oldColor = paint.color
            paint.color = dotColor[i]
            canvas.drawCircle(((left + right)/2 - leftMost).toFloat(),bottom + radius, radius, paint)
            paint.color = oldColor
            leftMost += 24
        }
    }
}