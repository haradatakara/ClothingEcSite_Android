package com.example.clothingecsite_30.viewModel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.CartListRepository

class CartListViewModelFactory :ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartListViewModel::class.java)) {
            return CartListViewModel(
                cartListRepository = CartListRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}