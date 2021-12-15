package com.devansab.begnn.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devansab.begnn.R
import com.devansab.begnn.utils.DebugLog
import com.devansab.begnn.utils.UnitConverter
import com.devansab.begnn.viewmodels.LoginViewModel
import com.google.android.gms.common.SignInButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.hbb20.CountryCodePicker
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

//eyJhbGciOiJSUzI1NiIsImtpZCI6IjE1MjU1NWEyMjM3MWYxMGY0ZTIyZjFhY2U3NjJmYzUwZmYzYmVlMGMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbWFqb3IxLTk5YTRjIiwiYXVkIjoibWFqb3IxLTk5YTRjIiwiYXV0aF90aW1lIjoxNjM1NDQxMjgxLCJ1c2VyX2lkIjoiQlpQeW1UaEFWaVNhYm9BZ3dDd0JzNjk5bnoyMyIsInN1YiI6IkJaUHltVGhBVmlTYWJvQWd3Q3dCczY5OW56MjMiLCJpYXQiOjE2MzU1MTcyMTQsImV4cCI6MTYzNTUyMDgxNCwicGhvbmVfbnVtYmVyIjoiKzkxMTIzNDU2Nzg5MCIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzkxMTIzNDU2Nzg5MCJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.YarHujc_ry-zcslIeX7BniBl8CGDvfEIzeIqw19jotfTq8tIbNNTqUnvkJWEGQGPmf2tcZRQxOxMXmLeTC3B_20XdS3mCeWd6radrvDFaIQqMOTj40iML1eLxm6TIlopVPQ6-zD7af3jYReVhGGFrqKNz8geVnoGHldYm1-fEIBpO_TsmGjMa8QyUxS9or58OtVIibIjy3Q9m6Koe-vo9EA-3MDw7UGNr2UfoPx-FAVs5yib-5QdLXxXkGxgwdfkOSEE64xKn88VDjBrsDRnir-cY3lsnGQN4Bsk4-xWKvm2f0uQdSe3bukdZblaq4NDxc1lUG2TaiNF80qmK4RqWA
class LoginActivity : AppCompatActivity() {

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var alertDialog: AlertDialog? = null
    private var progressTitle: TextView? = null
    private var progressMessage: TextView? = null
    private val TAG = this::class.java.simpleName + "TAG"
    private lateinit var viewModel: LoginViewModel
    private lateinit var countryCodePicker: CountryCodePicker;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
    }

    private fun initViews() {
        countryCodePicker = findViewById(R.id.view_login_ccp)
        countryCodePicker.registerCarrierNumberEditText(
            findViewById<EditText>(R.id.et_login_phoneNumb)
        )
        val googleSignInBtn = findViewById<SignInButton>(R.id.btn_login_googleSignInBtn)

        val alertDialogLayout = LayoutInflater.from(this)
            .inflate(R.layout.layout_waiting_progress_dialog, null)
        alertDialog = MaterialAlertDialogBuilder(this)
            .setView(alertDialogLayout)
            .setCancelable(false)
            .create()

        progressTitle = alertDialogLayout.findViewById(R.id.tv_lwpd_title)
        progressMessage = alertDialogLayout.findViewById(R.id.tv_lwpd_message)


        //Customizing google sign in button.
        for (i in 0..googleSignInBtn.childCount) {
            val child: View = googleSignInBtn.getChildAt(i)
            if (child is TextView) {
                child.text = "Sign In with Google"
                child.textSize = 16F
                val dp15 = UnitConverter.dpToPx(this, 15);
                val dp30 = UnitConverter.dpToPx(this, 35);
                child.setPadding(dp30, dp15, dp30, dp15);
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

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[LoginViewModel::class.java]


    }

    private fun onSendOtpClick() {
        if (!countryCodePicker.isValidFullNumber) {
            Toasty.error(baseContext, "Phone number is not valid.").show()
            return
        }

        progressTitle?.text = "Sending OTP"
        progressMessage?.text = "Please wait, we are processing your request."
        alertDialog?.show()
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
        alertDialog?.show()
        signInWithOtpCredentials(loginCredentials)
    }

    private val otpCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            storedVerificationId = p0
            resendToken = p1
            alertDialog?.cancel()
            Toasty.success(baseContext, "OTP sent successfully").show()


            findViewById<CardView>(R.id.card_login_signInMethods).visibility = View.GONE
            findViewById<CardView>(R.id.card_login_submitOTP).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_login_optSentNumb).text =
                "OTP sent to ${countryCodePicker.fullNumberWithPlus}"
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
            DebugLog.i(TAG, "login error: ${p0.localizedMessage}")
        }
    }

    private fun signInWithOtpCredentials(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener {
                DebugLog.i(TAG, "login : after success}")
                setUserRegistrationObserver()
                viewModel.isUserRegistered()
            }
            .addOnFailureListener {
                alertDialog?.cancel()
                Toasty.error(baseContext, "Please try again after sometime").show()
                DebugLog.i(TAG, "login error: in failure ${it.localizedMessage}")
            }
    }

    private fun setUserRegistrationObserver() {
        viewModel.getRegisterUserLiveData().observe(this, Observer { isRegistered ->
            if (isRegistered) {
                viewModel.updateFcmToken();
                startActivity(Intent(baseContext, HomeActivity::class.java))
                alertDialog?.cancel()
                finish()
            } else {
                startActivity(Intent(baseContext, UserRegistrationActivity::class.java))
                alertDialog?.cancel()
                finish()
            }
        })
    }
}