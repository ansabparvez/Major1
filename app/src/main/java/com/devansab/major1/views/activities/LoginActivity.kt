package com.devansab.major1.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.devansab.major1.R
import com.devansab.major1.utils.UnitConverter
import com.google.android.gms.common.SignInButton
import com.hbb20.CountryCodePicker

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        initViews()
    }

    private fun initViews(){
        val googleSignInBtn = findViewById<SignInButton>(R.id.btn_login_googleSignInBtn)


        for(i in 0..googleSignInBtn.childCount){
            Log.i("ansab", "child: "+i);
            val child : View = googleSignInBtn.getChildAt(i)
            if(child is TextView){
                val textView = child as TextView
                textView.text = "Sign In with Google"
                textView.textSize = 16F
                val dp15 = UnitConverter.dpToPx(this, 15);
                val dp30 = UnitConverter.dpToPx(this, 35);
                textView.setPadding(dp30, dp15, dp30, dp15);

                Log.i("ansab", "child is tv: "+i);
                break
            }
        }
    }
}