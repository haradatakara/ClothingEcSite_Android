package com.example.clothingecsite_30.viewModel.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Address
import com.example.clothingecsite_30.repository.PurchaseRegisterIndividualRepository
import kotlinx.coroutines.launch

class AddressViewModel(private val purchaseRegisterIndividualRepository: PurchaseRegisterIndividualRepository): ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address> = _address

    fun fetchAddress(zipcode: Int) {
        viewModelScope.launch {
            _address.value = purchaseRegisterIndividualRepository.fetchAddress(zipcode)
        }
    }
}