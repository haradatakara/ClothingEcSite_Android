package com.example.clothingecsite_30.viewModel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.repository.CartListRepository
import kotlinx.coroutines.launch

class CartListViewModel(private val cartListRepository: CartListRepository) :
    ViewModel() {

    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList

    fun onClickDeleteItem(item: Item) {
        viewModelScope.launch {
            cartListRepository.onClickDeleteItem(item);
        }
    }
}