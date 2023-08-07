package com.example.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.data.repository.UserUiState
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.favorite.FavoriteActivity
import com.example.githubuser.ui.setting.SettingActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.themeSetting.collect { state ->
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }

        mainViewModel.uiState.observe(this) { uiState ->
            when (uiState){
                is UserUiState.Loading -> {
                    showLoading(true)
                }
                is UserUiState.Success -> {
                    setUser(uiState.data)
                    showLoading(false)
                }
                is UserUiState.Error -> {
                    Toast.makeText(this, uiState.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }


        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{ textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.searchUser(searchView.text.toString())
                    false
                }
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                menuItem.itemId
                when (menuItem.itemId) {
                    R.id.menu1 -> {
                        val goToFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(goToFavorite)
                    }

                    R.id.menu2 -> {
                        val goToSetting = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(goToSetting)
                    }
                }
                true
            }
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.listUser.observe(this){dataUsers ->
            setUser(dataUsers).toString()
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }


    private fun setUser(dataUsers: List<ItemsItem?>){
        val adapter = UserAdapter()
        adapter.submitList(dataUsers)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}