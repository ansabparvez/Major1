package com.devansab.major1.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.utils.MainApplication
import com.devansab.major1.utils.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.util.HashMap

class UserRepository(val application: Application) {

    private val isUserRegisteredLiveData = MutableLiveData<Boolean>();
    private val isUserNameAvailableLiveData = MutableLiveData<Boolean>();
    private val userRegistrationLiveData = MutableLiveData<Boolean>();

    public fun isUserRegistered() {
        val user = FirebaseAuth.getInstance().currentUser ?: return;
        DebugLog.i(this, "getting token")
        user.getIdToken(false).addOnSuccessListener {
            DebugLog.i("ansab", "token: " + it.token);
            checkRegisteredUser(it.token.toString())
            SharedPrefManager.getInstance(application).setAuthToken(it.token.toString());
        }
    }


    private fun checkRegisteredUser(token: String) {
        val url =
            "https://us-central1-major1-99a4c.cloudfunctions.net/userV1/isRegistrationFinished"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
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
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    public fun getRegisterUserLiveData(): LiveData<Boolean> {
        return isUserRegisteredLiveData
    }

    public fun getIsUserNameAvailableLiveData(): LiveData<Boolean> {
        return isUserNameAvailableLiveData
    }

    public fun getUserRegistrationLiveData(): LiveData<Boolean> {
        return userRegistrationLiveData
    }

    public fun checkUserNameAvailable(userName: String) {
        val url =
            "https://us-central1-major1-99a4c.cloudfunctions.net/userV1/isUserNameAvailable?userName=$userName"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", "response: ($response.toString())")
                    if (response.getBoolean("success")) {
                        isUserNameAvailableLiveData.value =
                            response.getBoolean("isUserNameAvailable")
                    } else {
                        isUserNameAvailableLiveData.value = false
                    }
                },
                Response.ErrorListener { }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    val token = SharedPrefManager.getInstance(application).getAuthToken();
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            };
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    public fun registerUser(userData: HashMap<String, String>) {
        val url =
            "https://us-central1-major1-99a4c.cloudfunctions.net/userV1/completeRegistration"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", response.toString())
                    userRegistrationLiveData.value = response.getBoolean("success")
                },
                Response.ErrorListener { }
            ) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    return JSONObject(userData as Map<String, String>).toString().toByteArray()
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    val token = SharedPrefManager.getInstance(application).getAuthToken();
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            };
        MainApplication.instance.addToRequestQueue(jsonObjectRequest);
    }

}