package com.example.oopproject

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.oopproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val navController = binding.frgNav.getFragment<NavHostFragment>().navController
        binding.bottomNav.setupWithNavController(navController)
        enableEdgeToEdge()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.signinFragment -> binding.bottomNav.visibility = View.GONE
                else -> binding.bottomNav.visibility = View.VISIBLE
            }
        }
        setContentView(binding.root)
    }
}