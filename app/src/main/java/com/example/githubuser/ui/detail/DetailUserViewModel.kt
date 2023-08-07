package com.example.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.model.DetailUserResponse
import com.example.githubuser.data.remote.ApiConfig
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _listDetailUser = MutableLiveData<DetailUserResponse>()
    val listDetailUser: LiveData<DetailUserResponse> = _listDetailUser

    private val _listFollowing = MutableLiveData<List<ItemsItem?>>()
    val listFollowing : LiveData<List<ItemsItem?>> = _listFollowing

    private val _listFollower = MutableLiveData<List<ItemsItem?>>()
    val listFollower : LiveData<List<ItemsItem?>> = _listFollower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<UserEntity?>()
    val detailUser: LiveData<UserEntity?> = _detailUser

    companion object{
        private const val TAG = "DetailUserViewModel"
        private const val USER_ID = ""
    }

    init{
        getDetailUsername(USER_ID)
    }

    fun getDetailUsername(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(user)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listDetailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollower(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addFavorite(){
        viewModelScope.launch {
            val user = UserEntity(
                username = _listDetailUser.value?.login.toString(),
                avatarUrl = _listDetailUser.value?.avatarUrl
            )
            userRepository.addFavorite(user)

            getLocalUser(_listDetailUser.value?.login.toString())
        }
    }

    fun deleteFavorite(){
        viewModelScope.launch {
            val user = UserEntity(
                username = _listDetailUser.value?.login.toString(),
                avatarUrl = _listDetailUser.value?.avatarUrl
            )
            userRepository.deleteFavorite(user)

            getLocalUser(_listDetailUser.value?.login.toString())
        }
    }

    fun getLocalUser(username: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(username)
            _detailUser.postValue(user)
        }
    }
}