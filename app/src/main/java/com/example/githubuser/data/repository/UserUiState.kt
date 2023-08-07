package com.example.githubuser.data.repository

sealed class UserUiState<out R> private constructor() {
    data class Success<out T>(val data: T) : UserUiState<T>()
    data class Error(val error: String) : UserUiState<Nothing>()
    object Loading : UserUiState<Nothing>()
}

