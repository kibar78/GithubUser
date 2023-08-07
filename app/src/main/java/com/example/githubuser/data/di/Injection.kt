package com.example.githubuser.data.di

import android.content.Context
import com.example.githubuser.data.local.UserRoomDatabase
import com.example.githubuser.data.remote.ApiConfig
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.ui.setting.SettingPreferences
import com.example.githubuser.ui.setting.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserRoomDatabase.getDatabase(context)
        val dao = database.userDao()
        val pref = SettingPreferences.getInstance(context.dataStore)

        return UserRepository.getInstance(apiService, dao, pref)
    }
}