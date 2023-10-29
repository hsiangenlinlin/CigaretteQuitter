package com.example.cigarettequitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Guideline
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.cigarettequitter.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment),
            drawerLayout
        )

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
        // set toolbar title as app name
        binding.toolbar.title = getString(R.string.app_name)

        //implementing bottom navigation
        binding.bottomNavigation.setupWithNavController(navController)

        // handle exception when user did not initialize info
//        val sharedViewModel: SharedViewModel by viewModels()
//        lifecycleScope.launch {
//            val initInfoCount = withContext(Dispatchers.IO) {
//                sharedViewModel.database.initInfoDao().getInitInfoCount()
//            }
//            binding.bottomNavigation.menu.getItem(0).isEnabled = initInfoCount > 0
//            binding.bottomNavigation.menu.getItem(1).isEnabled = initInfoCount > 0
//            binding.bottomNavigation.menu.getItem(2).isEnabled = initInfoCount > 0
//            binding.bottomNavigation.menu.getItem(3).isEnabled = initInfoCount > 0

       // }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item) || item.onNavDestinationSelected(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp(appBarConfiguration)
    }
}