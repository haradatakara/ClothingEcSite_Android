package com.example.clothingecsite_30.viewModel.purchaseComfirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.repository.ItemMenuListRepository
import kotlinx.coroutines.launch

class ItemMenuListViewModel(private val itemMenuListRepository: ItemMenuListRepository) :
    ViewModel() {

    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList

    fun fetchItemList() {
        viewModelScope.launch {
            _itemList.value = itemMenuListRepository.fetchItemList()
        }
    }
}