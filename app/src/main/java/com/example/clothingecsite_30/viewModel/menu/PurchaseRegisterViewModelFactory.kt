package com.example.clothingecsite_30.viewModel.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.PurchaseRegisterIndividualRepository

class PurchaseRegisterViewModelFactory :ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchaseRegisterViewModel::class.java)) {
            return PurchaseRegisterViewModel(
                purchaseRegisterViewModel = PurchaseRegisterIndividualRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}