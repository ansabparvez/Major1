package com.devansab.begnn.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.devansab.begnn.R
import com.devansab.begnn.databinding.FragmentSettingsBinding
import com.devansab.begnn.utils.Const
import com.devansab.begnn.utils.MainApplication
import com.devansab.begnn.utils.ShareProfileUtil
import com.devansab.begnn.utils.SharedPrefManager
import com.devansab.begnn.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {
    private var rootView: View? = null
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(MainApplication.instance)
        )[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    private fun initViews() {
        val toolbar: Toolbar = binding.toolbarSettingsToolbar
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Settings"
        val sharedPrefManager = SharedPrefManager.getInstance(requireContext())
        binding.tvFullName.text = sharedPrefManager.getUserData()[Const.KEY_USER_NAME]
        binding.tvUserName.text = sharedPrefManager.getUserData()[Const.KEY_USER_UNAME]
        binding.shareProfile.setOnClickListener { ShareProfileUtil.share(requireContext()) }
        binding.deleteAccount.setOnClickListener { initDeleteAccount() }
    }

    private fun initDeleteAccount() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account from app.")
            .setPositiveButton("Yes") { _, _ ->

            }
            .setNegativeButton("No") { _, _ ->

            }
            .create()
            .show()
    }

}