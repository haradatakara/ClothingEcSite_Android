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
import com.example.clothingecsite_30.model.authentication.register.RegisterUser
import com.example.clothingecsite_30.repository.RegisterRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * 会員登録に関するViewModel
 */
class RegisterViewModel
    (private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthenticationResult>()
    val registerResult: LiveData<AuthenticationResult> = _registerResult

    val name = MutableLiveData<String>()

    var profileImageUri = MutableLiveData<Uri>()

    var gender = MutableLiveData<String>()

    val birthYear = MutableLiveData<String>()

    val birthMonth = MutableLiveData<String>()

    val birthDay = MutableLiveData<String>()

    val email = MutableLiveData<String>()

    val zipcode = MutableLiveData<String>()

    val prefectureCity = MutableLiveData<String>()

    val mansion = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val oneMorePassword = MutableLiveData<String>()

    val isConsentChecked = MutableLiveData<Boolean>()

    //会員登録
    @RequiresApi(Build.VERSION_CODES.O)
    fun register(

    ) {
        var isValid = true

        if (!isUserNameValid(name.value)) isValid = false

        if (!isEmailValid(email.value)) isValid = false

        if (!isZipCodeValid(zipcode.value)) isValid = false

        if (!isPrefectureCityValid(prefectureCity.value)) isValid = false

        if (!isBirthYearValid(birthYear.value)) isValid = false

        if (!isBirthMonthValid(birthMonth.value)) isValid = false

        if (!isBirthDayValid(birthDay.value)) isValid = false

        if (!isPasswordValid(password.value)) isValid = false

        if (!matchPass(password.value, oneMorePassword.value)) isValid = false

        if (!isConsentCheckValid(isConsentChecked.value)) isValid = false

        if (isGenderValid(gender.value) == false) isValid = false

        if (isValid) {
            //登録処理

            val registerUser = RegisterUser(
                profileImageUri.value.toString(),
                gender.value!!,
                "${birthYear.value}/${birthMonth.value}/${birthDay.value}",
                email.value!!,
                zipcode.value!!,
                prefectureCity.value!!,
                mansion.value,
                name.value!!,
                password.value!!
            )

            viewModelScope.launch {
                when (val result = registerRepository.register(registerUser)) {
                    is Result.Success -> {
                        _registerResult.value =
                            AuthenticationResult(success = LoggedInUserView(displayName = result.data.username))
                    }
                    else -> {
                        _registerResult.value =
                            AuthenticationResult(error = R.string.deplicate_mail)
                        RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
                    }
                }
            }
        } else {
            _registerForm.value =
                RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
        }
    }

    // 会員同意チェックバリデーション
    fun isConsentCheckValid(isConsentChecked: Boolean?): Boolean {
        var isValid = false
        if (isConsentChecked == false || isConsentChecked == null) {
            _registerForm.value =
                RegisterFormState(consentCheckError = R.string.invalid_not_check_consent)
        } else {
            isValid = true
        }
        return isValid
    }

    //性別セレクトボックスバリデーション
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

    // ユーザーネームバリーデーション
    fun isUserNameValid(username: String?): Boolean {
        var isValid = false
        if (username.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_not_input)
        } else {
            isValid = true
        }
        return isValid
    }

    //Emailバリデーション
    fun isEmailValid(email: String?): Boolean {
        var isValid = false
        if (email.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(mailAddressError = R.string.invalid_not_input)
        } else if (
            !Regex(
                "^[a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}\$"
            ).matches(email)
        ) {
            _registerForm.value =
                RegisterFormState(mailAddressError = R.string.invalid_email_zenkaku)
        } else {
            isValid = true
        }

        return isValid
    }

    // 郵便番号バリデーション
    fun isZipCodeValid(address: String?): Boolean {
        var isValid = false
        if (address?.length != 7) {
            _registerForm.value = RegisterFormState(zipcodeError = R.string.invalid_address)
        } else {
            isValid = true
        }
        return isValid
    }

    // 住所バリデーション
    fun isPrefectureCityValid(prefectureCity: String?): Boolean {
        var isValid = false
        if (prefectureCity.isNullOrBlank()) {
            _registerForm.value =
                RegisterFormState(prefectureCityError = R.string.invalid_not_input)
        } else {
            isValid = true
        }
        return isValid
    }

    //パスワードバリデーション
    fun isPasswordValid(pass: String?): Boolean {
        var isValid = false
        //デフォルトで半角入力しか受け付けない仕様なのでチェックなし
        //大文字と記号を含むかどうか
        if (pass.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_not_input)
        } else if (!Regex("^(?=.*[A-Z])(?=.*[.?/-])[a-zA-Z0-9.?/-]{8,24}\$").matches(pass)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_input_password)
        } else {
            isValid = true
        }

        return isValid
    }


    //誕生日バリデーション
    fun isBirthDayValid(birthDay: String?): Boolean {
        var isValid = false
        if (birthDay.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(birthDayError = R.string.invalid_not_input)
        } else if (!birthDay.let { Regex("[+-]?\\d+").matches(it) }) {
            _registerForm.value =
                RegisterFormState(birthDayError = R.string.invalid_birthday_birthMonth)
        } else {
            if (birthDay.toInt() > 31 || 1 > birthDay.toInt()) {
                _registerForm.value =
                    RegisterFormState(birthDayError = R.string.invalid_birthday_birthMonth)
            } else {
                isValid = true
            }
        }
        return isValid
    }

    //誕生年バリデーション
    @RequiresApi(Build.VERSION_CODES.O)
    fun isBirthYearValid(birthYear: String?): Boolean {
        var isValid = false
        if (birthYear.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(birthYearError = R.string.invalid_not_input)
        } else if (!birthYear.let { Regex("[+-]?\\d+").matches(it) }) {
            _registerForm.value =
                RegisterFormState(birthYearError = R.string.invalid_birthday_birthMonth)
        } else {
            if (birthYear.toInt() > LocalDate.now().year) {
                _registerForm.value =
                    RegisterFormState(birthYearError = R.string.invalid_birthday_birthMonth)
            } else {
                isValid = true
            }
        }
        return isValid
    }

    //誕生月バリデーション
    @RequiresApi(Build.VERSION_CODES.O)
    fun isBirthMonthValid(birthMonth: String?): Boolean {
        var isValid = false
        if (birthMonth.isNullOrBlank()) {
            _registerForm.value = RegisterFormState(birthMonthError = R.string.invalid_not_input)
        } else {
            if (!birthMonth.let { Regex("[+-]?\\d+").matches(it) } || (birthMonth.toInt() > 12 || 1 > birthMonth.toInt())) {
                _registerForm.value =
                    RegisterFormState(birthMonthError = R.string.invalid_birthday_birthMonth)
            } else {
                isValid = true
            }
        }
        return isValid
    }

    //パスワードが一致しているか
    private fun matchPass(pass: String?, oneMorePass: String?): Boolean {
        var isValid = false
        if (pass != oneMorePass) {
            _registerForm.value =
                RegisterFormState(matchPassError = R.string.invalid_not_match_pass)
        } else {
            isValid = true
        }
        return isValid
    }


}