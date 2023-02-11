package com.example.clothingecsite_30.viewModel.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.repository.ItemMenuListRepository
import kotlinx.coroutines.launch

/**
 * 押下された商品情報に関するViewModel
 */
class ItemViewModel(private val itemMenuListRepository: ItemMenuListRepository) :
    ViewModel() {

    private val _item = MutableLiveData<Item?>()
    val item: MutableLiveData<Item?> = _item

    fun fetchItem(docId: String) {
        viewModelScope.launch {
            _item.postValue(itemMenuListRepository.fetchItem(docId))
        }
    }
    fun deleteItem() {
        _item.postValue(null)
    }
}