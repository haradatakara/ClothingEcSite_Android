package com.example.clothingecsite_30.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.databinding.ActivityMyProfileBinding
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.IOException

/**
 * プロフィール更新ページ
 */
class MyProfileActivity : AppCompatActivity() {

    private var _binding: ActivityMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            openImage(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        //loginUser情報を取得
        loginViewModel.fetchLoginUser()
        //loginUser情報を取得してきた後の処置
        loginViewModel.loginUser.observe(this) {
            println(it)
        }
        //イメージボタン押下
        binding.ivProfileUserImage.setOnClickListener {
            resultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    private fun openImage(uri: Uri?) {
        try {
            val fireStorage = Firebase.storage
            val storageRef = fireStorage.reference.child("images/user-images")
            storageRef
                .child("${Firebase.auth.uid}.png")
                .putFile(uri!!)
                .addOnFailureListener {
                }
                .addOnSuccessListener {
                    binding.ivProfileUserImage.setImageURI(uri)
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}