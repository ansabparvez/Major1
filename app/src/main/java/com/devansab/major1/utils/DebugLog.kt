package com.devansab.major1.utils

import android.util.Log
import com.devansab.major1.BuildConfig

class DebugLog {
    companion object {
        public fun i(tag: Any, message: String) {
            if (!BuildConfig.DEBUG)
                return

            var finalTag = tag::class.java.simpleName;
            if (tag is String)
                finalTag = tag
            Log.i(finalTag, message);
        }

        public fun e(tag: String, message: String) {
            if (!BuildConfig.DEBUG)
                return

            var finalTag = tag::class.java.simpleName;
            if (tag is String)
                finalTag = tag
            Log.e(finalTag, message);
        }
    }
}