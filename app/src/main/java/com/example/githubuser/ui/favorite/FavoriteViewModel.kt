package com.example.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.repository.UserRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository): ViewModel() {
    private val _listFavorite = MutableLiveData<List<UserEntity>>()
    val listFavorite: LiveData<List<UserEntity>> = _listFavorite

    fun getFavorite(){
        viewModelScope.launch {
            val user = repository.getAllFavorite()
            _listFavorite.postValue(user)
        }
    }
}