package com.devansab.begnn.utils

import android.content.Context
import android.content.Intent

class ShareProfileUtil {
    companion object {
        fun share(context: Context){
            val sharedPrefManager = SharedPrefManager.getInstance(context)
            val userName = sharedPrefManager.getUserData()[Const.KEY_USER_UNAME]
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I am now on Begnn app. Click on this link and start chatting with me https://devansab.com/begnn/?user=$userName")
            context.startActivity(Intent.createChooser(shareIntent, "Share to"))
        }
    }
}