package com.example.githubuser.data.repository

import com.example.githubuser.data.local.UserDao
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.model.GithubResponse
import com.example.githubuser.data.remote.ApiService
import com.example.githubuser.ui.setting.SettingPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val settingPreferences: SettingPreferences
){
   suspend fun getListUsers(query: String): GithubResponse {
        return apiService.getUser(query)
    }

    suspend fun addFavorite(user: UserEntity) = userDao.insert(user)

    suspend fun deleteFavorite(user: UserEntity) = userDao.delete(user)

    suspend fun getUser(username: String): UserEntity?{
    return userDao.getFavoriteUserByUsername(username)
    }

    suspend fun getAllFavorite(): List<UserEntity>{
    return userDao.getAllFavorite()
    }

    fun getThemeSettings(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }


    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UserDao,
            settingPreferences: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, newsDao, settingPreferences)
            }.also { instance = it }
    }

}