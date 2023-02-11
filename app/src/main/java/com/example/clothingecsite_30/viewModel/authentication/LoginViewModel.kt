package com.example.clothingecsite_30.viewModel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.R

import com.example.clothingecsite_30.repository.LoginRepository
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.data.authentication.LoginFormState
import com.example.clothingecsite_30.data.authentication.AuthenticationResult
import com.example.clothingecsite_30.model.authentication.register.User
import kotlinx.coroutines.*

/**
 * ログインに関するViewModel
 */
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<AuthenticationResult>()
    val loginResult: LiveData<AuthenticationResult> = _loginResult

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean> = _isLogout

    fun login(username: String, password: String) {
        var isValid = true
        if (!isUserNameValid(username)) isValid = false
        if (!isPasswordValid(username)) isValid = false

        viewModelScope.launch {
            if (isValid) {
                when (val result = loginRepository.login(username, password)) {
                    is com.example.clothingecsite_30.model.authentication.Result.Success -> {
                        _loginResult.value =
                            AuthenticationResult(success = LoggedInUserView(displayName = result.data.username))
                    }
                    else -> {
                        _loginResult.value = AuthenticationResult(error = R.string.login_failed)
                    }
                }
            } else {
                _loginResult.value =
                    AuthenticationResult(error = R.string.login_failed)
            }
        }

    }

    fun fetchLoginUser() {
        viewModelScope.launch {
            _loginUser.value = loginRepository.fetchLoginUser()
        }

    }
    fun logout() {
        viewModelScope.launch {
            _isLogout.postValue(loginRepository.logout())
            delay(2000)
        }
    }

    fun isUserNameValid(username: String): Boolean {
        var isValid = false
        if (!Regex(
                "^(([0-9a-zA-Z!#\$%&'*+-/=?^_`{}|~]"
                        + "+(.[0-9a-zA-Z!#\$%&'*+-/=?^_`{}|~]+)*)|(\"[^\"]*\"))@[0-9a-zA-Z!#\$%&'*+-/=?^_`{}|~]"
                        + "+(.[0-9a-zA-Z!#\$%&'*+-/=?^_`{}|~]+)*\$"
            ).matches(username)
        ) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_email_zenkaku)
        } else {
            isValid = true
        }

        return isValid
    }

    fun isPasswordValid(password: String): Boolean {
        if (password.isEmpty()) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        }
        return password.isNotEmpty()
    }
}