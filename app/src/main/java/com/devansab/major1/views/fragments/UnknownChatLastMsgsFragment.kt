package com.devansab.major1.views.fragments

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
import com.devansab.major1.R
import com.devansab.major1.adapters.ReceivedMessagesRVAdapter
import com.devansab.major1.data.AppDatabase
import com.devansab.major1.data.entities.LastMessage
import com.devansab.major1.utils.MainApplication
import com.devansab.major1.viewmodels.UnknownChatLastMsgsViewModel
import com.devansab.major1.views.activities.KnownUserChatActivity
import com.devansab.major1.views.activities.UnknownUserChatActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UnknownChatLastMsgsFragment : Fragment(), ReceivedMessagesRVAdapter.LastMessageClickListener {
    private lateinit var rootView: View;
    private lateinit var viewModel: UnknownChatLastMsgsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_unknown_chat_last_msgs, container, false)

        initViews();
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

        val appDatabase = AppDatabase.getInstance(requireContext())
        val lastMessageDao = appDatabase.lastMessageDao()

//        GlobalScope.launch {
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab", "this is1 from ansab",
//                    System.currentTimeMillis(), "Ansab Parvez", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab_2", "this is 1 from ansab_2",
//                    System.currentTimeMillis(), "John", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab_3", "this is 1 from ansab_3",
//                    System.currentTimeMillis(), "Doe", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab_2", "this is 2 from ansab_2",
//                    System.currentTimeMillis(), "John", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab_2", "this is 3 from ansab_2",
//                    System.currentTimeMillis(), "John", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab_4", "this is 1 from ansab_4",
//                    System.currentTimeMillis(), "Yuk", false
//                )
//            )
//
//            lastMessageDao.insertLastMessage(
//                LastMessage(
//                    "ansab", "this is last from ansab",
//                    System.currentTimeMillis(), "Ansab Parvez", false
//                )
//            )
//        }

        viewModel.viewModelScope.launch {
            viewModel.getAllAnonymousLastMessages().collect {
                displayLastMessages(ArrayList(it))
            }
        }
    }

    private fun displayLastMessages(messagesList: ArrayList<LastMessage>) {
        val rvLastMessages = rootView.findViewById<RecyclerView>(R.id.rv_reMsg_chatPreview);
        rvLastMessages.layoutManager = LinearLayoutManager(requireContext())
        val sentMessagesAdapter = ReceivedMessagesRVAdapter(messagesList, this)
        rvLastMessages.adapter = sentMessagesAdapter
        sentMessagesAdapter.notifyDataSetChanged()
    }

    override fun onLastMessageClick(lastMessage: LastMessage) {
        val intent = Intent(requireContext(), UnknownUserChatActivity::class.java)
        intent.putExtra("userName", lastMessage.userName)
        startActivity(intent)
    }
}