package com.example.clothingecsite_30.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.ActivityMyProfileBinding
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory
import com.example.clothingecsite_30.viewModel.authentication.UpdateViewModel
import com.example.clothingecsite_30.viewModel.authentication.UpdateViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView

/**
 * プロフィール更新ページ
 */
class MyProfileActivity : AppCompatActivity(), UserConfirmDialogFragment.DialogListener {

    private var _binding: ActivityMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var updateViewModel: UpdateViewModel

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            openImage(it)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        updateViewModel =
            ViewModelProvider(this, UpdateViewModelFactory())[UpdateViewModel::class.java]

        binding.viewModel = updateViewModel
        setupActionBar()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (loginViewModel.loginUser.value == null) {
            loginViewModel.fetchLoginUser()
        }

        binding.loading.visibility = View.VISIBLE

        //ユーザー情報取得後、プロフィール写真がある場合、Glideによって表示する
        loginViewModel.loginUser.observe(this) {
            binding.loading.visibility = View.GONE
            if(it != null) {
                (binding.etName as TextView).text = it.name
                (binding.etEmail as TextView).text = it.email
                if (it.image != "") {
                    Glide
                        .with(this)
                        .load(it.image)
                        .into(findViewById<CircleImageView>(R.id.iv_profile_user_image))
                }
            } else {

            }
        }

        updateViewModel.name.observe(this) {
            updateViewModel.isUserNameValid(it)
        }

        updateViewModel.email.observe(this) {
            updateViewModel.isEmailValid(it)
        }

        updateViewModel.updateResult.observe(this) {
            binding.loading.visibility = View.GONE

            it.success?.let {
                loginViewModel.logout()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(
                    applicationContext, "成功したよ！もう一度ログインしてね！", Toast.LENGTH_LONG
                ).show()
            }

            it.error?.let {
                Toast.makeText(
                    applicationContext, "あかんかったから、もう一度", Toast.LENGTH_LONG
                ).show()
            }
        }

        //イメージボタン押下
        binding.ivProfileUserImage.setOnClickListener {
            resultLauncher
                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //更新ボタン押下
        binding.btnUpdate.setOnClickListener {
            showDialog()
        }


    }

    /**
     * アクションバーの設定
     */
    private fun setupActionBar() {
        val appBar = findViewById<Toolbar>(R.id.toolbar_my_profile_activity)
        setSupportActionBar(appBar)
    }

    private fun openImage(uri: Uri?) {
        updateViewModel.profileImageUri.value = uri
        binding.ivProfileUserImage.setImageURI(uri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val dialogFragment: DialogFragment = UserConfirmDialogFragment()
        dialogFragment.show(supportFragmentManager, "my_dialog")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDialogMapReceive(
        dialog: DialogFragment,
        myMutableMap: Map<String, String>
    ) {
        updateViewModel.confirmEmail.value = myMutableMap["confirmEmail"]
        updateViewModel.confirmPassword.value = myMutableMap["confirmPass"]
        binding.loading.visibility = View.VISIBLE
        updateViewModel.update()
    }

}