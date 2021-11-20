package com.devansab.major1.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.utils.MainApplication
import com.google.firebase.auth.FirebaseAuth
import java.util.HashMap

class UserRepository(val application: Application) {

    private val isUserRegisteredLiveData = MutableLiveData<Boolean>();

    public fun isUserRegistered() {
        val user = FirebaseAuth.getInstance().currentUser ?: return;
        user.getIdToken(false).addOnSuccessListener {
            DebugLog.i("ansab", "token: " + it.token);
            checkRegisteredUser(it.token.toString())
        }
    }


    private fun checkRegisteredUser(token: String) {
        val url =
            "https://us-central1-major1-99a4c.cloudfunctions.net/userV1/isRegistrationFinished"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", response.toString())
                    if (response.getBoolean("success")) {
                        isUserRegisteredLiveData.value =
                            response.getBoolean("registrationFinished")
                    } else {
                        isUserRegisteredLiveData.value = false
                    }
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
        MainApplication.instance.getRequestQueue().add(jsonObjectRequest);
    }

    public fun getRegisterUserLiveData(): LiveData<Boolean> {
        return isUserRegisteredLiveData
    }

}