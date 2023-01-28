package com.example.clothingecsite_30.viewModel.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.LoginRepository
import com.example.clothingecsite_30.repository.RegisterRepository

class UpdateViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return UpdateViewModel(
                registerRepository = RegisterRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}