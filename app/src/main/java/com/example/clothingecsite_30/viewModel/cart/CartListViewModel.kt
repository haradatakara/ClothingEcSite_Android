package com.example.clothingecsite_30.viewModel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.repository.CartListRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class CartListViewModel(private val cartListRepository: CartListRepository) :
    ViewModel() {

//    private val _cartList = MutableLiveData<MutableList<Cart>>()
//    val cartList: LiveData<MutableList<Cart>> = _cartList
    var cartItem = MutableLiveData<Cart>()
    var cartListItem = MutableLiveData<MutableList<Cart>>()
    var canPurchaseDb = MutableLiveData<Boolean>()
    var canCartListOpen = MutableLiveData<Boolean>()

    fun onClickCartBtn() {
        viewModelScope.launch {
            val result = cartListRepository.fetchCartItems()
            cartListItem.value = cartListRepository.fetchItemInfo(result)
        }
    }

    fun onClickDeleteBtn(cart: Cart?): Boolean {
        var canDatabase = false
        viewModelScope.launch {
            canDatabase = cartListRepository.onClickDeleteItem(cart)
            cartListItem.value?.remove(cart)
        }
        return canDatabase
    }
}