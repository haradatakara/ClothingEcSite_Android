package com.example.clothingecsite_30.viewModel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.PurchaseRegisterIndividualRepository

class AddressViewModelFactory :ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(
                purchaseRegisterIndividualRepository = PurchaseRegisterIndividualRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}