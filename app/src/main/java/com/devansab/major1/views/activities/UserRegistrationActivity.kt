package com.devansab.major1.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.devansab.major1.R
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.viewmodels.UserRegistrationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.dmoral.toasty.Toasty

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var etName: EditText;
    private lateinit var etUsername: EditText;
    private lateinit var viewModel: UserRegistrationViewModel;
    private lateinit var alertDialog: AlertDialog
    private lateinit var progressTitle: TextView
    private lateinit var progressMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        initViews()
    }

    private fun initViews() {
        etName = findViewById(R.id.et_userRegistration_name)
        etUsername = findViewById(R.id.et_userRegistration_userName)
        findViewById<Button>(R.id.btn_userRegistration_createAccount)
            .setOnClickListener({ button -> registerUser() })

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        ).get(UserRegistrationViewModel::class.java)

        setUserNameAvailableObserver()

        val alertDialogLayout = LayoutInflater.from(this)
            .inflate(R.layout.layout_waiting_progress_dialog, null)
        alertDialog = MaterialAlertDialogBuilder(this)
            .setView(alertDialogLayout)
            .create()

        progressTitle = alertDialogLayout.findViewById(R.id.tv_lwpd_title)
        progressMessage = alertDialogLayout.findViewById(R.id.tv_lwpd_message)
        progressTitle.text = "Almost there..."
    }

    private fun setUserNameAvailableObserver() {
        viewModel.getUserNameAvailableLiveData().observe(this, { isAvailable ->
            alertDialog.cancel()
            DebugLog.i("ansab tag", "is available: $isAvailable");
            if (isAvailable) {
                progressMessage.text = "Creating your account"
                alertDialog.show()
                val userName = etUsername.text.toString().trim()
                val name = etName.text.toString().trim()
                setUserRegistrationObserver()
                viewModel.completeRegistration(name, userName)
            } else {
                Toasty.error(baseContext, "User name is not available.").show()
            }
        })
    }

    private fun setUserRegistrationObserver(){
        viewModel.getUserRegistrationLiveData().observe(this, { isRegistered ->
            alertDialog.cancel()
            if(isRegistered){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toasty.error(this, "Some error. Please try again later").show()
            }
        })
    }

    private fun registerUser() {
        val userName = etUsername.text.toString().trim()
        val name = etName.text.toString().trim()
        if (userName.isEmpty()) {
            Toasty.error(this, "Enter a valid user name").show()
            return
        }
        if (userName.length < 4) {
            Toasty.error(this, "Enter at least 3 characters for user name").show()
            return
        }
        if (name.isEmpty()) {
            Toasty.error(this, "Enter a valid name").show()
            return
        }
        if (name.length < 4) {
            Toasty.error(this, "Enter at least 3 characters in name").show()
            return
        }

        progressMessage.text = "Checking user name"
        alertDialog.show()
        viewModel.isUserNameAvailable(userName)
    }
}