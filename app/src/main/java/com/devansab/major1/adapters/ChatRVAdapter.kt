package com.devansab.major1.adapters

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import java.util.*

class ChatRVAdapter(private val chatList: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View;
//        if (viewType == TYPE_SENT) {
//            view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_message_sent, parent, false);
//            return SentChatHolder(view)
//        }
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_received, parent, false);
        return ReceivedChatHolder(view);
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val chat = chatList[position]
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = chat.time
//        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//        val time = dateFormat.format(calendar.time)
//
//        if (chat.type == TYPE_SENT) {
//            val sentHolder: SentChatHolder = holder as SentChatHolder;
//            sentHolder.message.text = chat.message
//            sentHolder.time.text = time;
//        } else if (chat.type == TYPE_RECEIVED) {
//            val recChatHolder: ReceivedChatHolder = holder as ReceivedChatHolder;
//            recChatHolder.message.text = chat.message
//            recChatHolder.time.text = time
//        }

    }

    override fun getItemCount(): Int {
        return chatList.size;
    }

    override fun getItemViewType(position: Int): Int {
        return 0;
        //return if (chatList[position].type == TYPE_SENT) TYPE_SENT else TYPE_RECEIVED;
    }

    class SentChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tv_msgSent_message)
        val time: TextView = itemView.findViewById(R.id.tv_msgSent_time)
    }

    class ReceivedChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tv_msgRcv_message)
        val time: TextView = itemView.findViewById(R.id.tv_msgRcv_time)
    }
}