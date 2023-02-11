package com.example.clothingecsite_30.viewModel.cart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.repository.CartListRepository
import kotlinx.coroutines.launch

/**
 * カートに関するViewModel
 */
class CartListViewModel(private val cartListRepository: CartListRepository) :
    ViewModel() {

    var cartItem = MutableLiveData<Cart>()
    var cartListItem = MutableLiveData<MutableList<Cart>>()
    var canCartListOpen = MutableLiveData<Boolean>()

    // カート追加
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickAddCartBtn() {
        viewModelScope.launch {
            cartListRepository.onClickAddCartBtn(cartItem)
            onClickCartBtn()
        }
    }

    // カート表示
    fun onClickCartBtn() {
        viewModelScope.launch {
            val result = cartListRepository.fetchCartItems()
            cartListItem.value = cartListRepository.fetchItemInfo(result)
        }
    }

    //カートアイテム削除
    fun onClickDeleteBtn(cart: Cart?) {
        viewModelScope.launch {
            canCartListOpen.value = cartListRepository.onClickDeleteItem(cart)
            cartListItem.value?.remove(cart)
        }
    }
}