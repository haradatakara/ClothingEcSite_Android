package com.example.clothingecsite_30.viewModel.purchaseComfirm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.data.authentication.AuthenticationResult
import com.example.clothingecsite_30.data.authentication.RegisterFormState
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.enum.DeliveryMethod
import com.example.clothingecsite_30.repository.PurchaseRegisterIndividualRepository
import kotlinx.coroutines.launch

/**
 * 購入登録情報に関するViewModel
 */
class PurchaseRegisterViewModel(private val purchaseRegisterViewModel: PurchaseRegisterIndividualRepository) :
    ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthenticationResult>()
    val registerResult: LiveData<AuthenticationResult> = _registerResult

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        username: String,
        email: String,
        zipcode: String,
        prefectureCity: String,
        mansion: String,
        deliveryMethod: DeliveryMethod?,
        deliveryDate: String
    ) {
        var isValid = true

        if (!isUserNameValid(username)) isValid = false

        if (!isEmailValid(email)) isValid = false

        if (!isAddressZipcodeValid(zipcode)) isValid = false

        if (!isAddressPrefectureCityValid(prefectureCity)) isValid = false

        if (
            deliveryMethod == DeliveryMethod.SPECIFY
            && !isDeliveryDateValid(deliveryDate)
        ) isValid = false

        if (isValid) {
            //登録処理
            val userInfo = hashMapOf(
                "username" to username,
                "email" to email,
                "zipcode" to zipcode
            )
            viewModelScope.launch {
//                val result = registerRepository.register(userInfo)
//                if (result is Result.Success) {
//                    _registerResult.value =
//                        AuthenticationResult(success = LoggedInUserView(displayName = result.data.username))
//                } else {
//                    _registerResult.value = AuthenticationResult(error = R.string.deplicate_mail)
//                    RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
//                }
            }
        } else {
            _registerForm.value =
                RegisterFormState(cannotRegisterError = R.string.invalid_can_not_register)
        }
    }

    fun isUserNameValid(username: String): Boolean {
        var isValid = false
        if (username.isBlank()) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else {
            isValid = true
        }
        return isValid
    }

    fun isEmailValid(email: String): Boolean {
        var isValid = false
        if (!Regex(
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

    fun isAddressZipcodeValid(address: String): Boolean {
        var isValid = false
        if (address.length != 7) {
            _registerForm.value = RegisterFormState(zipcodeError = R.string.invalid_address)
        } else {
            isValid = true
        }
        return isValid
    }

    fun isAddressPrefectureCityValid(address: String): Boolean {
        var isValid = false
        if (address.isBlank()) {
            _registerForm.value = RegisterFormState(prefectureCityError = R.string.invalid_not_input)
        } else {
            isValid = true
        }
        return isValid
    }

    fun isDeliveryDateValid(deliveryDate: String): Boolean {
        var isValid = false
        if (deliveryDate.isBlank()) {
            _registerForm.value = RegisterFormState(deliveryDateError = R.string.invalid_not_input)
        } else {
            isValid = true
        }
        return isValid
    }

}
