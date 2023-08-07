package com.example.githubuser.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.di.Injection
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.ui.detail.DetailUserViewModel
import com.example.githubuser.ui.favorite.FavoriteViewModel
import com.example.githubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)){
            return DetailUserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}