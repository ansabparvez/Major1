package com.devansab.begnn.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.devansab.begnn.viewmodels.MainViewModel
import com.devansab.begnn.R
import com.devansab.begnn.utils.DebugLog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.myLooper()!!).postDelayed({
            initViews()
        }, 1000)


        /*val message = Message(
            UUID.randomUUID().toString(), "this is message 1",
            System.currentTimeMillis(), "test", false, false
        )

        val message2 = Message(
            UUID.randomUUID().toString(), "this is message 2",
            System.currentTimeMillis(), "test", false, false
        )

        val message3 = Message(
            UUID.randomUUID().toString(), "this is message 3",
            System.currentTimeMillis(), "test", true, false
        )

        val message4 = Message(
            UUID.randomUUID().toString(), "this is message 4",
            System.currentTimeMillis(), "test", true, false
        )

        val message5 = Message(
            UUID.randomUUID().toString(), "this is message 5",
            System.currentTimeMillis(), "test", false, false
        )

        val message6 = Message(
            UUID.randomUUID().toString(), "this is message 6",
            System.currentTimeMillis(), "test", true, false
        )

        val message7 = Message(
            UUID.randomUUID().toString(), "this is message 7",
            System.currentTimeMillis(), "test", true, false
        )

        val message8 = Message(
            UUID.randomUUID().toString(), "this is message 8",
            System.currentTimeMillis(), "test", false, false
        )

        val message9 = Message(
            UUID.randomUUID().toString(), "this is message 9",
            System.currentTimeMillis(), "test", false, false
        )

        val message10 = Message(
            UUID.randomUUID().toString(), "this is message 10",
            System.currentTimeMillis(), "test", true, false
        )

        GlobalScope.launch {
            MessageRepository(application).insertMessage(message)
            MessageRepository(application).insertMessage(message2)
            MessageRepository(application).insertMessage(message3)
            MessageRepository(application).insertMessage(message4)
            MessageRepository(application).insertMessage(message5)
            MessageRepository(application).insertMessage(message6)
            MessageRepository(application).insertMessage(message7)
            MessageRepository(application).insertMessage(message8)
            MessageRepository(application).insertMessage(message9)
            MessageRepository(application).insertMessage(message10)
        }*/

        /*GlobalScope.launch {
            UserRepository(application).insertUser(
                User("ansab", "Ansab Parvez", false)
            )
            UserRepository(application).insertUser(
                User("test", "Mr. Test", false)
            )
        }*/
    }

    private fun initViews() {
        val viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )[MainViewModel::class.java]

        //FirebaseAuth.getInstance().signOut()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            DebugLog.i(this, "user is null")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        DebugLog.i(this, "user is not null")

        viewModel.isUserRegistered()

        viewModel.getRegisterUserLiveData().observe(this, { isRegistered ->
            if (isRegistered) {
                DebugLog.i(this, "user is registered")
                val intent = Intent(this, HomeActivity::class.java)
                startFinalActivity(intent)
            } else {
                DebugLog.i(this, "user is not registered")
                val intent = Intent(this, UserRegistrationActivity::class.java)
                startFinalActivity(intent)
            }
        })
    }

    private fun startFinalActivity(intent: Intent) {
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(intent)
            finish()
        }, 2000)
    }
}