package com.devansab.major1.utils

import android.util.Log
import com.devansab.major1.BuildConfig

class DebugLog {
    companion object{
        public fun i(tag : String, message : String){
            if(BuildConfig.DEBUG)
                Log.i(tag, message)
        }

        public fun e(tag : String, message : String){
            if(BuildConfig.DEBUG)
                Log.e(tag, message)
        }
    }
}