package com.devansab.begnn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.data.entities.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatRVAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT = 1;
        private const val TYPE_RECEIVED = 2;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View;
        if (viewType == TYPE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false);
            return SentChatHolder(view)
        }
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_received, parent, false);
        return ReceivedChatHolder(view);
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = message.time.toLong()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val time = dateFormat.format(calendar.time)

        val type = getItemViewType(position)
        if (type == TYPE_SENT) {
            val sentHolder: SentChatHolder = holder as SentChatHolder;
            sentHolder.message.text = message.text
            sentHolder.time.text = time
        } else if (type == TYPE_RECEIVED) {
            val recChatHolder: ReceivedChatHolder = holder as ReceivedChatHolder;
            recChatHolder.message.text = message.text
            recChatHolder.time.text = time
        }
    }

    override fun getItemCount(): Int {
        return messages.size;
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sentByMe) TYPE_SENT else TYPE_RECEIVED;
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