package com.example.clothingecsite_30.viewModel.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.R

import com.example.clothingecsite_30.repository.LoginRepository
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.data.authentication.LoginFormState
import com.example.clothingecsite_30.data.authentication.AuthenticationResult
import com.example.clothingecsite_30.data.authentication.RegisterFormState
import com.example.clothingecsite_30.model.authentication.Result
import com.example.clothingecsite_30.model.authentication.register.User
import com.example.clothingecsite_30.repository.RegisterRepository
import kotlinx.coroutines.*

class UpdateViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthenticationResult>()
    val registerResult: LiveData<AuthenticationResult> = _registerResult

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        username: String,
        email: String,
        address: String,
        birthYear: String,
        birthMonth: String,
        birthDay: String,
        pass: String,
        oneMorePass: String,
        isConsentChecked: Boolean,
        selectGender: String?
    ) {
        var isValid = true

        if (!isUserNameValid(username)) isValid = false

        if (!isEmailValid(email)) isValid = false

        if (!isAddressValid(address)) isValid = false

        if (isGenderValid(selectGender) == false) isValid = false

        if (isValid) {
            //登録処理
            val userInfo = hashMapOf(
                "username" to username,
                "email" to email,
                "password" to pass,
                "address" to address,
                "birth" to "${birthYear}/${birthMonth}/${birthDay}",
                "gender" to selectGender!!
            )
            viewModelScope.launch {
                when (val result = registerRepository.register(userInfo)) {
                    is Result.Success -> {
                        _registerResult.value =
                            AuthenticationResult(success = LoggedInUserView(displayName = result.data.username))
                    }
                    else -> {
                        _registerResult.value = AuthenticationResult(error = R.string.deplicate_mail)
                        RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
                    }
                }
            }
        } else {
            _registerForm.value =
                RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
        }
    }

    //登録ボタンが押された時に実行
    private fun isGenderValid(gender: String?): Boolean? {
        var isValid = false
        if (gender == null) {
            _registerForm.value =
                RegisterFormState(genderError = R.string.invalid_not_select_gender)
        } else {
            isValid = true
        }
        return isValid
    }

    fun isUserNameValid(username: String): Boolean {
        var isValid = false
        if (username.isBlank()) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_not_input)
        } else {
            isValid = true
        }

        return isValid
    }

    fun isEmailValid(email: String): Boolean {
        var isValid = false
        if (!Regex("^[a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}\$"
            ).matches(email)
        ) {
            _registerForm.value =
                RegisterFormState(mailAddressError = R.string.invalid_email_zenkaku)
        } else {
            isValid = true
        }

        return isValid
    }

    fun isAddressValid(address: String): Boolean {
        var isValid = false
        if (address.length != 7) {
            _registerForm.value = RegisterFormState(zipcodeError = R.string.invalid_address)
        } else {
            isValid = true
        }

        return isValid
    }



}