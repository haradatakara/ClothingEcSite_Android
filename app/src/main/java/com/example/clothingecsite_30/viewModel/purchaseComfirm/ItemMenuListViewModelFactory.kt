package com.example.clothingecsite_30.viewModel.purchaseComfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.repository.ItemMenuListRepository

class ItemMenuListViewModelFactory :ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemMenuListViewModel::class.java)) {
            return ItemMenuListViewModel(
                itemMenuListRepository = ItemMenuListRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}