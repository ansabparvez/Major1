package com.devansab.begnn.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.begnn.R
import com.devansab.begnn.adapters.SentMessagesRVAdapter
import com.devansab.begnn.data.entities.LastMessage
import com.devansab.begnn.utils.MainApplication
import com.devansab.begnn.viewmodels.KnownChatLastMsgsViewModel
import com.devansab.begnn.views.activities.KnownUserChatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KnownChatLastMsgsFragment : Fragment(), SentMessagesRVAdapter.LastMessageClickListener {
    private lateinit var rootView: View
    private lateinit var viewModel: KnownChatLastMsgsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_known_chat_last_msgs, container, false)

        initViews()
        return rootView
    }

    private fun initViews() {
        val toolbar: Toolbar? = rootView.findViewById(R.id.toolbar_sentMsg_toolbar)
        rootView.findViewById<FloatingActionButton>(R.id.fab_sentMsg_findUser)
            .setOnClickListener { showFindUserPopUp() }
        toolbar?.title = "People I Know"

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(MainApplication.instance)
        )[KnownChatLastMsgsViewModel::class.java]

        viewModel.viewModelScope.launch {
            viewModel.getAllUnAnonymousLastMessages().collect {
                displayLastMessages(ArrayList(it))
            }
        }
    }

    private fun showFindUserPopUp() {
        val findUserLayout = activity?.layoutInflater?.inflate(R.layout.layout_search_user, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(findUserLayout)
            .create()

        val etSearchUserName = findUserLayout?.findViewById<EditText>(R.id.et_searchUser_userName)
        val btnSearchUser = findUserLayout?.findViewById<Button>(R.id.btn_searchUser_search)

        setFindUserObserver()
        btnSearchUser?.setOnClickListener {
            viewModel.findUser(etSearchUserName?.text.toString())
        }

        alertDialog.show()
    }

    private fun setFindUserObserver() {
        viewModel.getFindUserLiveData().observe(viewLifecycleOwner, {
            if (it.success) {
                val intent = Intent(requireActivity(), KnownUserChatActivity::class.java)
                intent.putExtra("userName", it.user?.userName)
                intent.putExtra("name", it.user?.name)
                startActivity(intent)
                viewModel.viewModelScope.launch {
                    viewModel.insertUser(it.user!!)
                }
                //Toasty.success(requireContext(), it.user?.userName.toString()).show()
            } else {
                Toasty.error(requireContext(), it.error.toString()).show()
            }
        })
    }

    private fun displayLastMessages(messagesList: ArrayList<LastMessage>) {
        val rvLastMessages = rootView.findViewById<RecyclerView>(R.id.rv_sentMsg_chatPreview)
        if (messagesList.size == 0) {
            rootView.findViewById<ConstraintLayout>(R.id.constraintLayout_sentMsg_noMessage)
                .visibility = View.VISIBLE
            rootView.findViewById<Button>(R.id.btn_sentMsg_searchUser)
                .setOnClickListener { showFindUserPopUp() }
            rvLastMessages.visibility = GONE
            return
        }
        rootView.findViewById<ConstraintLayout>(R.id.constraintLayout_sentMsg_noMessage)
            .visibility = View.GONE
        rvLastMessages.visibility = VISIBLE
        rvLastMessages.layoutManager = LinearLayoutManager(requireContext())
        val sentMessagesAdapter = SentMessagesRVAdapter(messagesList, this)
        rvLastMessages.adapter = sentMessagesAdapter
        sentMessagesAdapter.notifyDataSetChanged()
    }

    override fun onLastMessageClick(lastMessage: LastMessage) {
        val intent = Intent(requireContext(), KnownUserChatActivity::class.java)
        intent.putExtra("userName", lastMessage.message.userName)
        intent.putExtra("name", lastMessage.name)
        startActivity(intent)
    }


}