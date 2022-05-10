package com.devansab.begnn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devansab.begnn.data.repositories.UserRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo by lazy { UserRepository(application) }

    fun deleteUserProfile() = userRepo.deleteUserProfile()

}