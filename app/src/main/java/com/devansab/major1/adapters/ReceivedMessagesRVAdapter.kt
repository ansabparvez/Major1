package com.devansab.major1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.data.entities.LastMessage

class ReceivedMessagesRVAdapter(
    private val lastMessage: List<LastMessage>,
    private val listener: LastMessageClickListener
) :
    RecyclerView.Adapter<ReceivedMessagesRVAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_chat, parent, false);
        return MessageHolder(view);
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = lastMessage[position];
        holder.tvName.text = message.name
        holder.tvDate.text = message.time.toString()
        holder.tvMessage.text = message.text
    }

    override fun getItemCount(): Int {
        return lastMessage.size;
    }

    //View Holder Class
    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_listChat_name);
        val tvDate: TextView = itemView.findViewById(R.id.tv_listChat_date);
        val tvMessage: TextView = itemView.findViewById(R.id.tv_listChat_message);

        init {
            itemView.setOnClickListener {
                listener.onLastMessageClick(lastMessage[absoluteAdapterPosition])
            }
        }
    }

    interface LastMessageClickListener {
        fun onLastMessageClick(lastMessage: LastMessage)
    }

}