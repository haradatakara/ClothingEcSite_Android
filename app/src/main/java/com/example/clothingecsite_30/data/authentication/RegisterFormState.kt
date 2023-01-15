package com.example.clothingecsite_30.data.authentication

data class RegisterFormState(
    val usernameError: Int? = null,
    val genderError: Int? = null,
    val cannotRegisterError: Int? = null,
    val passwordError: Int? = null,
    val oneMorePasswordError: Int? = null,
    val matchPassError: Int? = null,
    val mailAddressError: Int? = null,
    val zipcodeError: Int? = null,
    val prefectureCityError: Int? = null,
    val deliveryDateError: Int? = null,
    val birthDayError: Int? = null,
    val birthYearError: Int? = null,
    val birthMonthError: Int? = null,
    val consentCheckError: Int? = null,
) {
}