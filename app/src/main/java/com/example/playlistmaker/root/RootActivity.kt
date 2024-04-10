package com.example.playlistmaker.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRootBinding.inflate(layoutInflater) }
    private val viewModel: RootViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
        }
        viewModel.isBottomNavigationVisible.observe(this) {
            binding.bottomNavigationView.isVisible = it
        }
    }

}