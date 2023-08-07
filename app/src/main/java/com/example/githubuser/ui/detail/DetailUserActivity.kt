package com.example.githubuser.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionPagerAdapter
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.ui.main.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private val detailViewModel by viewModels<DetailUserViewModel>(){
        ViewModelFactory.getInstance(applicationContext)
    }
    private var favoriteStatus: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.getStringExtra(EXTRA_USERNAME) != null){
            val username = intent.getStringExtra(EXTRA_USERNAME)

            binding.fabFavorite.setOnClickListener {
                if (favoriteStatus){
                    detailViewModel.deleteFavorite()
                } else {
                    detailViewModel.addFavorite()
                }
            }
            detailViewModel.getDetailUsername(username.toString())
            detailViewModel.getLocalUser(username.toString())

            detailViewModel.detailUser.observe(this) {user ->
                if (user!=null){
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                    favoriteStatus = true
                }else{
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                    favoriteStatus = false
                }
            }
            detailViewModel.getDetailUsername(username.toString())
            detailViewModel.getLocalUser(username.toString())

            detailViewModel.listDetailUser.observe(this){user ->
                Glide.with(this)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(binding.ivAvatar)
                binding.tvName.text = user.name.toString()
                binding.tvUsername.text = user.login.toString()
                binding.tvFollowers.text = "${user.followers.toString()} Followers"
                binding.tvFollowing.text = "${user.following.toString()} Following"

                if (user.location == null){
                    binding.tvLocation.isGone
                } else{
                    binding.tvLocation.text = user.location.toString()
                }
            }
            val sectionsPagerAdapter = SectionPagerAdapter(this)
            sectionsPagerAdapter.username = username.toString()
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

        }

    }

    companion object{
        const val EXTRA_USERNAME = "user.name"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}