package com.devansab.major1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.data.entities.ChatPreview

class SentMessagesRVAdapter(private val chatPreviewList: List<ChatPreview>) :
    RecyclerView.Adapter<SentMessagesRVAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_chat, parent, false);
        return MessageHolder(view);
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val chatPreview = chatPreviewList[position];
        holder.tvName.text = chatPreview.userName;
        holder.tvDate.text = chatPreview.date;
        holder.tvMessage.text = chatPreview.lastMessage;
    }

    override fun getItemCount(): Int {
        return chatPreviewList.size;
    }


    //View Holder Class
    class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_listChat_name);
        val tvDate: TextView = itemView.findViewById(R.id.tv_listChat_date);
        val tvMessage: TextView = itemView.findViewById(R.id.tv_listChat_message);
    }

}