package com.devansab.major1.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devansab.major1.R
import com.devansab.major1.adapters.SentMessagesRVAdapter
import com.devansab.major1.data.entities.ChatPreview
import com.devansab.major1.utils.MainApplication
import com.devansab.major1.viewmodles.SentMessagesFragViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty

class SentMessagesFragment : Fragment() {
    private lateinit var rootView: View;
    private lateinit var viewModel: SentMessagesFragViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sent_messages, container, false)

        initViews();
        return rootView
    }

    private fun initViews() {
        val toolbar: Toolbar? = rootView.findViewById(R.id.toolbar_sentMsg_toolbar)
        rootView.findViewById<FloatingActionButton>(R.id.fab_sentMsg_findUser)
            .setOnClickListener { showFindUserPopUp() }
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = "Sent Messages"

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(MainApplication.instance)
        )[SentMessagesFragViewModel::class.java]

        val rvChatPreview = rootView.findViewById<RecyclerView>(R.id.rv_sentMsg_chatPreview)
        val chatPreviewList = ArrayList<ChatPreview>()
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab", "This is a text message", "15/12/2020"))
        chatPreviewList.add(ChatPreview("ansab last", "This is a text message", "15/12/2020"))

        val chatPreviewAdapter = SentMessagesRVAdapter(chatPreviewList)
        rvChatPreview?.layoutManager = LinearLayoutManager(context)
        rvChatPreview?.adapter = chatPreviewAdapter
        chatPreviewAdapter.notifyItemRangeInserted(0, chatPreviewList.size)
    }

    private fun showFindUserPopUp() {
        val findUserLayout = activity?.layoutInflater?.inflate(R.layout.layout_search_user, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(findUserLayout)
            .create()

        val etSearchUserName = findUserLayout?.findViewById<EditText>(R.id.et_searchUser_userName);
        val btnSearchUser = findUserLayout?.findViewById<Button>(R.id.btn_searchUser_search);

        setFindUserObserver()
        btnSearchUser?.setOnClickListener {
            viewModel.findUser(etSearchUserName?.text.toString())
        }

        alertDialog.show()
    }

    private fun setFindUserObserver(){
        viewModel.getFindUserLiveData().observe(this, Observer {
            if(it.success){
                Toasty.success(requireContext(), it.user?.userName.toString()).show()
            }else{
                Toasty.error(requireContext(), it.error.toString()).show()
            }
        })
    }
}