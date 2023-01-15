package com.example.clothingecsite_30.viewModel.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.repository.ItemMenuListRepository
import kotlinx.coroutines.launch

class ItemViewModel(private val itemMenuListRepository: ItemMenuListRepository) :
    ViewModel() {

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    fun fetchItem(docId: String) {
        viewModelScope.launch {
            _item.postValue(itemMenuListRepository.fetchItem(docId))
        }
    }
}