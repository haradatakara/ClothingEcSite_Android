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
import com.example.clothingecsite_30.model.authentication.Result
import com.example.clothingecsite_30.repository.RegisterRepository
import kotlinx.coroutines.launch
import java.time.LocalDate


class RegisterViewModel
    (private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthenticationResult>()
    val registerResult: LiveData<AuthenticationResult> = _registerResult

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        profileImageUri: Uri?,
        username: String,
        email: String,
        address: String,
        prefectureCity: String,
        mansion: String,
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

        if (!isBirthYearValid(birthYear)) isValid = false

        if (!isBirthMonthValid(birthMonth)) isValid = false

        if (!isBirthDayValid(birthDay)) isValid = false

        if (!isPasswordValid(pass)) isValid = false

        if (!isPasswordValid(oneMorePass)) isValid = false

        if (!matchPass(pass, oneMorePass)) isValid = false

        if (!isConsentCheckValid(isConsentChecked)) isValid = false

        if (isGenderValid(selectGender) == false) isValid = false

        if (isValid) {
            //登録処理
            val userInfo = hashMapOf(
                "image" to profileImageUri.toString(),
                "username" to username,
                "email" to email,
                "password" to pass,
                "address" to address,
                "prefecture" to prefectureCity,
                "mansion" to mansion,
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

    fun isConsentCheckValid(isConsentChecked: Boolean): Boolean {
        var isValid = false
        if (!isConsentChecked) {
            _registerForm.value =
                RegisterFormState(consentCheckError = R.string.invalid_not_check_consent)
        } else {
            isValid = true
        }
        return isValid
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

    fun isPasswordValid(pass: String): Boolean {
        var isValid = false
        //デフォルトで半角入力しか受け付けない仕様なのでチェックなし
        //大文字と記号を含むかどうか
        if (pass.isBlank()) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_not_input)
        } else if (!Regex("^(?=.*[A-Z])(?=.*[.?/-])[a-zA-Z0-9.?/-]{8,24}\$").matches(pass)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_input_password)
        } else {
            isValid = true
        }

        return isValid
    }


    fun isBirthDayValid(birthDay: String): Boolean {
        var isValid = false
        if (birthDay.isBlank()) {
            _registerForm.value = RegisterFormState(birthDayError = R.string.invalid_not_input)
        } else if (!Regex("[+-]?\\d+").matches(birthDay)) {
            _registerForm.value =
                RegisterFormState(birthDayError = R.string.invalid_birthday_birthMonth)
        } else if (birthDay.toInt() > 31 || 1 > birthDay.toInt()) {
            _registerForm.value =
                RegisterFormState(birthDayError = R.string.invalid_birthday_birthMonth)
        } else {
            isValid = true
        }

        return isValid
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isBirthYearValid(birthYear: String): Boolean {
        var isValid = false
        if (birthYear.isBlank()) {
            _registerForm.value = RegisterFormState(birthYearError = R.string.invalid_not_input)
        } else if (!Regex("[+-]?\\d+").matches(birthYear)) {
            _registerForm.value =
                RegisterFormState(birthYearError = R.string.invalid_birthday_birthMonth)
        } else if (birthYear.toInt() > LocalDate.now().year) {
            _registerForm.value =
                RegisterFormState(birthYearError = R.string.invalid_birthday_birthMonth)
        } else {
            isValid = true
        }

        return isValid
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isBirthMonthValid(birthMonth: String): Boolean {
        var isValid = false
        if (birthMonth.isBlank()) {
            _registerForm.value = RegisterFormState(birthMonthError = R.string.invalid_not_input)
        } else if (
            !Regex("[+-]?\\d+").matches(birthMonth)
            || (birthMonth.toInt() > 12 || 1 > birthMonth.toInt())) {
            _registerForm.value =
                RegisterFormState(birthMonthError = R.string.invalid_birthday_birthMonth)
        } else {
            isValid = true
        }

        return isValid
    }

    private fun matchPass(pass: String, oneMorePass: String): Boolean {
        var isValid = false

        if (pass != oneMorePass) {
            _registerForm.value = RegisterFormState(matchPassError = R.string.invalid_not_match_pass)
        } else {
            isValid = true
        }

        return isValid
    }


}