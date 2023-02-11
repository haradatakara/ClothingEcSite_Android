package com.example.clothingecsite_30.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.ActivityMainBinding
import com.example.clothingecsite_30.view.dialog.CartListDialogFragment
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
/**
 * メニューリストフラグメントや商品詳細フラグメントを表示するアクティビティ
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
        binding.navView.setNavigationItemSelectedListener(this)

        //すでにログイン済みだが、ユーザー情報を取得していない場合
        //ユーザー情報を取得する
        if (loginViewModel.loginUser.value == null) {
            loginViewModel.fetchLoginUser()
        }

        //ユーザー情報取得後、プロフィール写真がある場合、Glideによって表示する
        loginViewModel.loginUser.observe(this) {
            if(it != null) {
                Glide.with(this)
                    .load(it.image)
                    .into(findViewById<CircleImageView>(R.id.iv_user_image))
            }
        }

        setupActionBar()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val dialog = CartListDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }

    /**
     * アクションバーの設定
     */
    private fun setupActionBar() {
        val appBar = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(appBar)
        appBar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        appBar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    /**
     * ナビゲーションの動き
     */
    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * ナビゲーションに関する設定
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**
     * ナビゲーションのメニュー押下時
     */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
//            R.id.nav_my_profile -> {
//                val intent = Intent(this, MyProfileActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            }
            R.id.nav_sign_out -> {
                loginViewModel.logout()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}

