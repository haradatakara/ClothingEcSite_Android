package com.example.clothingecsite_30.viewModel.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.ItemMenuListRepository

class ItemModelFactory :ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(
                itemMenuListRepository = ItemMenuListRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}