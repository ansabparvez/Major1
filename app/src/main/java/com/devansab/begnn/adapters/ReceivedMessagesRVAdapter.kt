package com.devansab.begnn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.data.LastMessage

class ReceivedMessagesRVAdapter(
    private val lastMessagesList: List<LastMessage>,
    private val listener: LastMessageClickListener
) :
    RecyclerView.Adapter<ReceivedMessagesRVAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_chat, parent, false);
        return MessageHolder(view);
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val lastMessage = lastMessagesList[position];
        holder.tvName.text = lastMessage.name
        holder.tvDate.text = lastMessage.message.time.toString()
        holder.tvMessage.text = lastMessage.message.text
    }

    override fun getItemCount(): Int {
        return lastMessagesList.size;
    }

    //View Holder Class
    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_listChat_name);
        val tvDate: TextView = itemView.findViewById(R.id.tv_listChat_date);
        val tvMessage: TextView = itemView.findViewById(R.id.tv_listChat_message);

        init {
            itemView.setOnClickListener {
                listener.onLastMessageClick(lastMessagesList[absoluteAdapterPosition])
            }
        }
    }

    interface LastMessageClickListener {
        fun onLastMessageClick(lastMessage: LastMessage)
    }

}