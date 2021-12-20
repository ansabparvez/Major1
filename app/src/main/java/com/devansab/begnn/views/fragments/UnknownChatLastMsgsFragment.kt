package com.devansab.begnn.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.adapters.ReceivedMessagesRVAdapter
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.utils.MainApplication
import com.devansab.begnn.viewmodels.UnknownChatLastMsgsViewModel
import com.devansab.begnn.views.activities.UnknownUserChatActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UnknownChatLastMsgsFragment : Fragment(), ReceivedMessagesRVAdapter.LastMessageClickListener {
    private lateinit var rootView: View
    private lateinit var viewModel: UnknownChatLastMsgsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_unknown_chat_last_msgs, container, false)

        initViews()
        return rootView
    }

    private fun initViews() {
        val toolbar: Toolbar? = rootView.findViewById(R.id.toolbar_reMsg_toolbar)
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = "Received Messages"

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(MainApplication.instance)
        )[UnknownChatLastMsgsViewModel::class.java]

        viewModel.viewModelScope.launch {
            viewModel.getAllAnonymousLastMessages().collect {
                displayLastMessages(ArrayList(it))
            }
        }
    }

    private fun displayLastMessages(messagesList: ArrayList<LastMessage>) {
        val rvLastMessages = rootView.findViewById<RecyclerView>(R.id.rv_reMsg_chatPreview)
        rvLastMessages.layoutManager = LinearLayoutManager(requireContext())
        val sentMessagesAdapter = ReceivedMessagesRVAdapter(messagesList, this)
        rvLastMessages.adapter = sentMessagesAdapter
        sentMessagesAdapter.notifyDataSetChanged()
    }

    override fun onLastMessageClick(lastMessage: LastMessage) {
        val intent = Intent(requireContext(), UnknownUserChatActivity::class.java)
        intent.putExtra("userName", lastMessage.message.userName)
        startActivity(intent)
    }
}