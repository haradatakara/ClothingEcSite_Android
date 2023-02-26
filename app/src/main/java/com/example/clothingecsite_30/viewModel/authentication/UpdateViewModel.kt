package com.example.clothingecsite_30.viewModel.authentication

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.clothingecsite_30.R

import com.example.clothingecsite_30.data.authentication.AuthenticationResult
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.data.authentication.RegisterFormState
import com.example.clothingecsite_30.model.authentication.UpdateUser

import com.example.clothingecsite_30.model.authentication.register.User
import com.example.clothingecsite_30.repository.UpdateRepository
import kotlinx.coroutines.launch

/**
 * 会員情報更新に関する
 */
class UpdateViewModel(private val updateRepository: UpdateRepository) : ViewModel() {

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    private val _updateForm = MutableLiveData<RegisterFormState>()
    val updateFormState: LiveData<RegisterFormState> = _updateForm

    private val _updateResult = MutableLiveData<AuthenticationResult>()
    val updateResult: LiveData<AuthenticationResult> = _updateResult

    var profileImageUri = MutableLiveData<Uri>()

    val name = MutableLiveData<String>()

    val email = MutableLiveData<String>()

    val confirmPassword = MutableLiveData<String>()

    val confirmEmail = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun update() {
        var isValid = true

        if (!isUserNameValid(name.value)) isValid = false

        if (!isEmailValid(email.value)) isValid = false

        if (isValid) {
            //登録処理
            val updateInfo = UpdateUser(
                profileImageUri.value.toString(),
                name.value.toString(),
                email.value.toString()
            )

            viewModelScope.launch {
                when (val result = updateRepository.update(updateInfo, confirmPassword.value.toString(), confirmEmail.value.toString())) {
                    is com.example.clothingecsite_30.model.authentication.Result.Success -> {
                        _updateResult.value =
                            AuthenticationResult(success = LoggedInUserView(displayName = result.data.username))
                    }
                    else -> {
                        _updateResult.value = AuthenticationResult(error = R.string.deplicate_mail)
                    }
                }
            }
        } else {
            _updateForm.value =
                RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
        }
    }


    // ユーザーネームバリーデーション
    fun isUserNameValid(username: String?): Boolean {
        var isValid = false
        if (username.isNullOrBlank()) {
            _updateForm.value = RegisterFormState(usernameError = R.string.invalid_not_input)
        } else {
            isValid = true
        }
        return isValid
    }


    //Emailバリデーション
    fun isEmailValid(email: String?): Boolean {
        var isValid = false
        if (email.isNullOrBlank()) {
            _updateForm.value = RegisterFormState(mailAddressError = R.string.invalid_not_input)
        } else if (
            !Regex(
                "^[a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}\$"
            ).matches(email)
        ) {
            _updateForm.value =
                RegisterFormState(mailAddressError = R.string.invalid_email_zenkaku)
        } else {
            isValid = true
        }

        return isValid
    }

}