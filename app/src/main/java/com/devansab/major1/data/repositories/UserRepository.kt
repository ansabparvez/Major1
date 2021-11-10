package com.devansab.major1.data.repositories

import android.app.Application
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.devansab.major1.utils.DebugLog
import com.google.firebase.auth.FirebaseAuth
import java.util.HashMap

class UserRepository(application: Application) {

    public fun isUserRegistered() {
        val user = FirebaseAuth.getInstance().currentUser ?: return;
        user.getIdToken(false).addOnSuccessListener {
            DebugLog.i("ansab", "token: " + it.token);
        }
    }

    private fun checkRegisteredUser(token: String) {
        val url =
            "https://us-central1-major1-99a4c.cloudfunctions.net/userV1/isRegistrationFinished"
        val networkRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->

                },
                Response.ErrorListener { }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            };
    }

}