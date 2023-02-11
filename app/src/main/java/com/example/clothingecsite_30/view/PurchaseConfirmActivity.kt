package com.example.clothingecsite_30.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.clothingecsite_30.R

/**
 * 購入登録ページ
 */
class PurchaseConfirmActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_confirm)
        val navController = findNavController(R.id.nav_host_fragment_content_purchase_confirm)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBar()
    }

    private fun setupActionBar() {
        val appBar = findViewById<Toolbar>(R.id.toolbar_my_profile_activity)
        setSupportActionBar(appBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
    }
}