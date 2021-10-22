package com.devansab.major1.views.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.devansab.major1.R
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.utils.UnitConverter
import com.google.android.gms.common.SignInButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.hbb20.CountryCodePicker
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var alertDialog: AlertDialog? = null
    private var progressTitle: TextView? = null
    private var progressMessage: TextView? = null
    private val TAG = this::class.java.simpleName + "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
    }

    private fun initViews() {
        val googleSignInBtn = findViewById<SignInButton>(R.id.btn_login_googleSignInBtn)

        val alertDialogLayout = LayoutInflater.from(this)
            .inflate(R.layout.layout_waiting_progress_dialog, null)
        alertDialog = MaterialAlertDialogBuilder(this)
            .setView(alertDialogLayout)
            .create()

        progressTitle = alertDialogLayout.findViewById(R.id.tv_lwpd_title)
        progressMessage = alertDialogLayout.findViewById(R.id.tv_lwpd_message)


        //Customizing google sign in button.
        for (i in 0..googleSignInBtn.childCount) {
            val child: View = googleSignInBtn.getChildAt(i)
            if (child is TextView) {
                val textView = child as TextView
                textView.text = "Sign In with Google"
                textView.textSize = 16F
                val dp15 = UnitConverter.dpToPx(this, 15);
                val dp30 = UnitConverter.dpToPx(this, 35);
                textView.setPadding(dp30, dp15, dp30, dp15);
                break
            }
        }

        findViewById<Button>(R.id.btn_login_sendOTP)
            .setOnClickListener {
                onSendOtpClick();
            }
        findViewById<Button>(R.id.btn_login_submitOTP)
            .setOnClickListener {
                onSubmitOtpClick();
            }
    }

    private fun onSendOtpClick() {
        val countryCodePicker = findViewById<CountryCodePicker>(R.id.view_login_ccp)
        val etPhoneNum = findViewById<EditText>(R.id.et_login_phoneNumb);
        countryCodePicker.registerCarrierNumberEditText(etPhoneNum)

        if (!countryCodePicker.isValidFullNumber) {
            Toasty.error(baseContext, "Phone number is not valid.").show()
            return
        }

        progressTitle?.text = "Sending OTP"
        progressMessage?.text = "Please wait, we are processing your request."
        alertDialog?.show()
        findViewById<CardView>(R.id.card_login_signInMethods).visibility = View.GONE
        findViewById<CardView>(R.id.card_login_submitOTP).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tv_login_optSentNumb).text =
            "OTP sent to ${countryCodePicker.fullNumberWithPlus}"
        sendOtp(countryCodePicker.fullNumberWithPlus)
    }

    private fun sendOtp(number: String) {
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(otpCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun onSubmitOtpClick() {
        val otp = findViewById<EditText>(R.id.et_login_otp).text.toString();
        if (otp.isEmpty())
            Toasty.error(baseContext, "OTP is empty").show()

        val loginCredentials = PhoneAuthProvider.getCredential(storedVerificationId!!, otp);
        progressTitle?.text = "Signing In"
        progressMessage?.text = "Verifying the OTP"
        signInWithOtpCredentials(loginCredentials)
    }

    private val otpCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            storedVerificationId = p0
            resendToken = p1
            alertDialog?.cancel()
            Toasty.success(baseContext, "OTP sent successfully").show()
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            progressTitle?.text = "Signing In"
            progressMessage?.text = "Signing you in automatically"
            alertDialog?.show()
            signInWithOtpCredentials(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            alertDialog?.cancel()
            if (p0 is FirebaseAuthInvalidCredentialsException)
                Toasty.error(baseContext, "Invalid request.").show()
            Toasty.error(baseContext, "Some error occurred, try again after sometime").show()
            DebugLog.i(TAG, "login error: ${p0.localizedMessage}" )
        }
    }

    private fun signInWithOtpCredentials(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener {
                DebugLog.i(TAG, "login : after success}" )

            }
            .addOnFailureListener {
                DebugLog.i(TAG, "login error: in failure ${it.localizedMessage}" )
            }
    }
}