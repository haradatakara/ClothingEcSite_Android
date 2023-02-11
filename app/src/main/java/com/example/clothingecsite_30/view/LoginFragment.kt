package com.example.clothingecsite_30.view

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.util.textWatcher.CustomTextWatcher
import com.example.clothingecsite_30.util.textWatcher.CustomTextWatcherListener
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentLoginBinding
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory

/**
 * ログイン画面に関するフラグメント
 */
class LoginFragment : Fragment(), CustomTextWatcherListener {

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

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.loginBtn
        val loadingProgressBar = binding.loading

        enterTextChangedListener(usernameEditText, passwordEditText)

        // 入力時のバリデーションエラーメッセージ出力
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            loginFormState.usernameError?.let {
                usernameEditText.error = getString(it)
            }
            loginFormState.passwordError?.let {
                passwordEditText.error = getString(it)
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
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(), passwordEditText.text.toString()
                )
            }
            false
        }

        // ログインボタン押下時の処理
        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    private fun enterTextChangedListener(
        emailEditText: EditText,
        passwordEditText: EditText,
    ) {
        passwordEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this,
                    this@LoginFragment
                )
            )
        }
        emailEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this,
                    this@LoginFragment
                )
            )
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

    override fun afterTextChanged(view: View, p0: Editable?) {
        val inputStr: String = p0.toString()
        when (view.id) {
            R.id.username -> {
                loginViewModel.isUserNameValid(inputStr)
            }
            R.id.password -> {
                loginViewModel.isPasswordValid(inputStr)
            }

        }
    }
    override fun beforeTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}