package com.example.clothingecsite_30.viewModel.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.LoginRepository
import com.example.clothingecsite_30.repository.RegisterRepository
import com.example.clothingecsite_30.repository.UpdateRepository

class UpdateViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
            return UpdateViewModel(
                updateRepository = UpdateRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}