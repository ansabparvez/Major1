package com.devansab.begnn.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devansab.begnn.data.AppDatabase
import com.devansab.begnn.databinding.FragmentSettingsBinding
import com.devansab.begnn.utils.*
import com.devansab.begnn.viewmodels.SettingsViewModel
import com.devansab.begnn.views.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        binding.logout.setOnClickListener { initLogout() }
    }

    private fun initDeleteAccount() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account from app?")
            .setPositiveButton("Yes") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("No") { _, _ ->

            }
            .create()
            .show()
    }

    private fun deleteAccount() {

        viewModel.deleteUserProfile().observe(viewLifecycleOwner, Observer {
            when (it.type) {
                ApiResult.PROGRESS -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                ApiResult.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    FirebaseAuth.getInstance().currentUser!!.delete()
                    SharedPrefManager.getInstance(requireContext()).clearData()
                    viewModel.viewModelScope.launch {
                        AppDatabase.getInstance(requireContext()).clearAllTables()
                    }

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    val error = it.error ?: "Some error. Try again later."
                    Snackbar.make(requireContext(), binding.root, error, Snackbar.LENGTH_LONG)
                        .show()
                }
            }

        })
    }

    private fun initLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout? This will delete all your chats.")
            .setPositiveButton("Logout") { _, _ ->
                SharedPrefManager.getInstance(requireContext()).clearData()
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    AppDatabase.getInstance(requireContext()).clearAllTables()
                }
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("Cancel") { _, _ ->

            }
            .create()
            .show()
    }

}