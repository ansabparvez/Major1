package com.devansab.major1.data.repositories

import android.app.Application
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.devansab.major1.data.AppDatabase
import com.devansab.major1.data.daos.LastMessageDao
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.data.entities.Message
import com.devansab.major1.utils.Const
import com.devansab.major1.utils.DebugLog
import com.devansab.major1.utils.MainApplication
import com.devansab.major1.utils.SharedPrefManager
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.util.HashMap

class MessageRepository(private val application: Application) {

    private val appDatabase = AppDatabase.getInstance(application)
    private var lastMessageDao: LastMessageDao = appDatabase.lastMessageDao();
    private var messageDao = appDatabase.messageDao();

    fun getAllLastMessages(isAnonymous: Int):
            Flow<List<LastMessage>> = lastMessageDao.getAllLastMessages(isAnonymous)

    fun getAllMessagesOfUser(username: String, isAnonymous: Int):
            Flow<List<Message>> = messageDao.getAllMessagesOfUser(username, isAnonymous)

    suspend fun sendMessageToKnownUser(message: Message, receiverId: String) {
        messageDao.insertMessage(message)

        val messageMap = HashMap<String, String>()
        messageMap["messageId"] = message.messageId
        messageMap["text"] = message.text
        messageMap["time"] = message.time.toString()
        messageMap["userName"] = SharedPrefManager.getInstance(application)
            .getUserData()[Const.KEY_USER_ANON_ID].toString();
        messageMap["receiverId"] = receiverId

        val messageUrl =
            "https://us-central1-major1-99a4c.cloudfunctions.net/messages/sendMessageToKnownUser"

        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, messageUrl, null,
                Response.Listener { response ->
                    DebugLog.i("ansab", response.toString())
                },
                Response.ErrorListener {
                    DebugLog.i("ansab", it.localizedMessage)
                }
            ) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    return JSONObject(messageMap as Map<String, String>).toString().toByteArray()
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