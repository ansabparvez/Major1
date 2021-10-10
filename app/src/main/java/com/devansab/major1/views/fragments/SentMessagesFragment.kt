package com.devansab.major1.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.devansab.major1.R

class SentMessagesFragment : Fragment() {
    private var rootView: View? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sent_messages, container, false)

        initViews();
        return rootView
    }

    private fun initViews() {
        val toolbar : Toolbar? = rootView?.findViewById(R.id.toolbar_sentMsg_toolbar)
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = "Sent Messages"

        rootView?.findViewById<TextView>(R.id.tv_sentMsg_text)?.text="sent changed"
    }
}