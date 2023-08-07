package com.example.githubuser.ui.main

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.data.repository.UserUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainViewModel (private val userRepository: UserRepository): ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem?>>()
    val listUser: LiveData<List<ItemsItem?>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uiState = MutableLiveData<UserUiState<List<ItemsItem?>>>()
    val uiState: LiveData<UserUiState<List<ItemsItem?>>> = _uiState

    val themeSetting: Flow<Boolean> = userRepository.getThemeSettings()

    companion object{
        private const val TAG = "MainViewModel"
        private const val USER_ID = "rizki"
    }

    init{
        searchUser(USER_ID)
    }

    fun searchUser(query: String) {
        _uiState.value = UserUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = UserUiState.Success(userRepository.getListUsers(query).items!!)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(e.message.toString())
            }
        }

    }
}