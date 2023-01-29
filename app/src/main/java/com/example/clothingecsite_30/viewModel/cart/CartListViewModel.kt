package com.example.clothingecsite_30.viewModel.cart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.repository.CartListRepository
import kotlinx.coroutines.launch

class CartListViewModel(private val cartListRepository: CartListRepository) :
    ViewModel() {

    //    private val _cartList = MutableLiveData<MutableList<Cart>>()
//    val cartList: LiveData<MutableList<Cart>> = _cartList
    var cartItem = MutableLiveData<Cart>()
    var cartListItem = MutableLiveData<MutableList<Cart>>()
    var canPurchaseDb = MutableLiveData<Boolean>()
    var canCartListOpen = MutableLiveData<Boolean>()

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun onClickPurchaseBtn() {
//        viewModelScope.launch {
//            canPurchaseDb.postValue(cartListRepository.purchaseComp(cartItem))
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickAddCartBtn() {
        viewModelScope.launch {
            cartListRepository.onClickAddCartBtn(cartItem)
            onClickCartBtn()
        }
    }

    fun onClickCartBtn() {
        viewModelScope.launch {
            val result = cartListRepository.fetchCartItems()
            cartListItem.value = cartListRepository.fetchItemInfo(result)
        }
    }

    fun onClickDeleteBtn(cart: Cart?) {
        viewModelScope.launch {
            canCartListOpen.value = cartListRepository.onClickDeleteItem(cart)
            cartListItem.value?.remove(cart)
        }
    }
}