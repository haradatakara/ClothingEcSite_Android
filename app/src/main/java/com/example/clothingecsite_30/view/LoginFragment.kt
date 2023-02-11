package com.example.clothingecsite_30.view

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentLoginBinding
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory

/**
 * ログイン画面のフラグメント
 */
class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        binding.viewModel = loginViewModel

        val loadingProgressBar = binding.loading

        //入力バリデーションチェック
        loginViewModel.email.observe(viewLifecycleOwner) {
            loginViewModel.isEmailValid(it)
        }
        loginViewModel.password.observe (viewLifecycleOwner) {
            loginViewModel.isPasswordValid(it.toString())
        }

        // 入力時のバリデーションエラーメッセージ出力
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            loginFormState.emailError?.let {
                binding.username.error = getString(it)
            }
            loginFormState.passwordError?.let {
                binding.password.error = getString(it)
            }
        })

        // ログインボタン押下後の結果
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
            loginResult ?: return@Observer
            loadingProgressBar.visibility = View.GONE
            loginResult.error?.let {
                showLoginFailed(it)
            }
            loginResult.success?.let {
                updateUiWithUser(it)
                startActivity(Intent(this.requireContext(), MainActivity::class.java))
            }
        })

        // Enterキーを押した時の処理
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login()
            }
            false
        }

        // ログインボタン押下時の処理
        binding.loginBtn.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login()
        }

        binding.gotoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }


    //ログイン成功時のトースト
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    // ログイン失敗時のトースト
    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}