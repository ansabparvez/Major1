package com.devansab.begnn.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.adapters.ReceivedMessagesRVAdapter
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.utils.Const.Companion.KEY_USER_UNAME
import com.devansab.begnn.utils.MainApplication
import com.devansab.begnn.utils.SharedPrefManager
import com.devansab.begnn.viewmodels.UnknownChatLastMsgsViewModel
import com.devansab.begnn.views.activities.UnknownUserChatActivity
import es.dmoral.toasty.Toasty
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
        toolbar?.title = "Anonymous Messages"

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
        if (messagesList.size == 0) {
            showShareUserUI()
            rvLastMessages.visibility = View.GONE
            return
        }
        rootView.findViewById<ConstraintLayout>(R.id.constraintLayout_reMsg_noMessageLayout)
            .visibility = View.GONE
        rvLastMessages.visibility = VISIBLE
        rvLastMessages.layoutManager = LinearLayoutManager(requireContext())
        val sentMessagesAdapter = ReceivedMessagesRVAdapter(messagesList, this)
        rvLastMessages.adapter = sentMessagesAdapter
        sentMessagesAdapter.notifyDataSetChanged()
    }

    private fun showShareUserUI() {
        rootView.findViewById<ConstraintLayout>(R.id.constraintLayout_reMsg_noMessageLayout)
            .visibility = View.VISIBLE
        val tvUserName = rootView.findViewById<TextView>(R.id.tv_reMsg_uNameCopy)
        val userName = (SharedPrefManager.getInstance(requireContext())
            .getUserData()[KEY_USER_UNAME])
        tvUserName.text = userName
        val btnShareUserName = rootView.findViewById<Button>(R.id.btn_reMsg_shareUName)

        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        tvUserName.setOnClickListener {
            val clipData = ClipData.newPlainText("username", userName)
            clipboardManager.setPrimaryClip(clipData)
            Toasty.success(requireContext(), "Copied to clipboard").show()
        }

        btnShareUserName.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "My user name $userName")
            startActivity(Intent.createChooser(shareIntent, "Share to"))
        }
    }

    override fun onLastMessageClick(lastMessage: LastMessage) {
        val intent = Intent(requireContext(), UnknownUserChatActivity::class.java)
        intent.putExtra("userName", lastMessage.message.userName)
        startActivity(intent)
    }
}