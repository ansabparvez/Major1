package com.devansab.begnn.utils

import android.util.Log
import com.devansab.begnn.BuildConfig

class DebugLog {
    companion object {
        fun i(tag: Any, message: String) {
            if (!BuildConfig.DEBUG)
                return

            var finalTag = tag::class.java.simpleName
            if (tag is String)
                finalTag = tag
            Log.i(finalTag, message)
        }

        fun e(tag: Any, message: String) {
            if (!BuildConfig.DEBUG)
                return

            var finalTag = tag::class.java.simpleName
            if (tag is String)
                finalTag = tag
            Log.e(finalTag, message)
        }
    }
}