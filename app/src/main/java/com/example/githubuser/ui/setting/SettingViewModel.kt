package com.example.githubuser.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: UserRepository): ViewModel(){
    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSettings().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }
}