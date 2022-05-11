package com.devansab.begnn.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.devansab.begnn.data.AppDatabase
import com.devansab.begnn.data.daos.UserDao
import com.devansab.begnn.data.entities.User
import com.devansab.begnn.utils.*
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.HashMap

class UserRepository(val application: Application) {

    private val isUserRegisteredLiveData = MutableLiveData<Boolean>()
    private val isUserNameAvailableLiveData = MutableLiveData<Boolean>()
    private val userRegistrationLiveData = MutableLiveData<Boolean>()
    private var findUserLiveData = MutableLiveData<FindUserModel>()
    private val appDatabase = AppDatabase.getInstance(application)
    private var userDao: UserDao = appDatabase.userDao()

    fun isUserRegistered() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        DebugLog.i(this, "getting token")
        user.getIdToken(true).addOnSuccessListener {
            DebugLog.i("ansab", "auth token: " + it.token)
            checkRegisteredUser(it.token.toString())
            SharedPrefManager.getInstance(application).setAuthToken(it.token.toString())
        }
    }


    private fun checkRegisteredUser(token: String) {
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/isRegistrationFinished"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    if (response.getBoolean("success")) {
                        isUserRegisteredLiveData.value =
                            response.getBoolean("registrationFinished")
                        if (response.getBoolean("registrationFinished")) {
                            saveUserDataToSharedPref(response.getJSONObject("userData"))
                        }
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
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    fun getRegisterUserLiveData(): LiveData<Boolean> {
        return isUserRegisteredLiveData
    }

    fun getIsUserNameAvailableLiveData(): LiveData<Boolean> {
        return isUserNameAvailableLiveData
    }

    fun getUserRegistrationLiveData(): LiveData<Boolean> {
        return userRegistrationLiveData
    }

    fun getFindUserLiveData(): LiveData<FindUserModel> {
        return findUserLiveData
    }

    fun resetFindUserLiveData() {
        findUserLiveData = MutableLiveData<FindUserModel>()
    }

    fun checkUserNameAvailable(userName: String) {
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/isUserNameAvailable?userName=$userName"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
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
                    val token = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    fun registerUser(userData: HashMap<String, String>) {
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/completeRegistration"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", response.toString())
                    userRegistrationLiveData.value = response.getBoolean("success")
                    if (response.getBoolean("success")) {
                        saveUserDataToSharedPref(response.getJSONObject("userData"))
                    }
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
                    val token = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    suspend fun findUser(userName: String) {
        val user = userDao.getUserByUsername(userName)
        DebugLog.i("ansabLog", "user in local $user")
        if (user == null)
            findUserFromServer(userName)
        else
            findUserLiveData.postValue(FindUserModel(true, null, user))
    }

    private fun findUserFromServer(userName: String) {
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/findUser?userName=$userName"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansabLog", "response: ($response.toString())")
                    if (response.getBoolean("success")) {
                        val user = User(
                            response.getString("userName"),
                            response.getString("name"), false
                        )
                        findUserLiveData.value = FindUserModel(true, null, user)
                    } else {
                        findUserLiveData.value = FindUserModel(
                            false,
                            response.getString("error")
                        )
                    }
                },
                Response.ErrorListener {
                    DebugLog.i("ansabLog", "find user api error: ${it.message}")
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    val token = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun fetchAndStoreAnonymousUserData(anonymousId: String) {
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/getAnonymousUserData?anonymousId=$anonymousId"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", "response: ($response.toString())")
                    if (response.getBoolean("success")) {
                        val user = User(
                            response.getString("anonymousId"),
                            response.getString("anonymousName"), true
                        )
                        GlobalScope.launch(Dispatchers.IO) {
                            userDao.insertUser(user)
                        }
                    }
                },
                Response.ErrorListener { }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    val token = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $token"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }


    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)

    data class FindUserModel(
        val success: Boolean,
        var error: String? = null,
        var user: User? = null
    )

    private fun saveUserDataToSharedPref(userData: JSONObject) {
        val userDataMap = HashMap<String, String>()
        userDataMap[Const.KEY_USER_NAME] = userData.getString("name")
        userDataMap[Const.KEY_USER_UNAME] = userData.getString("userName")
        userDataMap[Const.KEY_USER_ANON_ID] = userData.getString("anonymousId")

        SharedPrefManager.getInstance(application).setUserData(userDataMap)
        DebugLog.i("ansab", userDataMap.toString())
    }

    fun fetchAndUpdateFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            updateFcmToken(it)
        }
    }

    fun updateFcmToken(token: String) {
        val userData = HashMap<String, String>()
        userData["fcmToken"] = token
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/updateFcmToken"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", response.toString())
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
                    val authToken = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $authToken"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
    }

    fun deleteUserProfile(): LiveData<ApiResult<Boolean>> {
        val data = MutableLiveData<ApiResult<Boolean>>()
        data.postValue(ApiResult(ApiResult.PROGRESS))
        val url =
            "https://us-central1-begnn-app.cloudfunctions.net/userV1/deleteUser"
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener {
                    val success = it.getBoolean("success")
                    data.value = ApiResult(ApiResult.SUCCESS, success)
                },
                Response.ErrorListener {
                    Log.i("ansabLog", "api failed ${it.message}")
                    data.value = ApiResult(ApiResult.ERROR, error = it.localizedMessage)
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    val authToken = SharedPrefManager.getInstance(application).getAuthToken()
                    params["Authorization"] = "Bearer $authToken"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        MainApplication.instance.addToRequestQueue(jsonObjectRequest)
        return data
    }

}