package com.devansab.major1.utils

import android.content.Context
import android.util.DisplayMetrics

class UnitConverter {
    companion object{
        fun dpToPx(context : Context, dp : Int) : Int {
            val displayMetrics : DisplayMetrics = context.resources.displayMetrics;
            val px = displayMetrics.density*dp;
            return (px + 0.5F).toInt()
        }
    }
}