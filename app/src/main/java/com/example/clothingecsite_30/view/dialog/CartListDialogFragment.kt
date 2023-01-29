package com.example.clothingecsite_30.view.dialog

import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.databinding.FragmentCartListDialogBinding
import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.util.CartAdapterListener
import com.example.clothingecsite_30.util.CartListAdapter
import com.example.clothingecsite_30.viewModel.cart.CartListViewModel
import com.example.clothingecsite_30.viewModel.cart.CartListViewModelFactory

class CartListDialogFragment : BottomSheetDialogFragment(), CartAdapterListener {

    private lateinit var lvMenu: ListView
    private lateinit var itemMenuListViewModel: CartListViewModel
    private lateinit var cartListViewModel: CartListViewModel
    private lateinit var cartItemAdapter: CartListAdapter

    private var _binding: FragmentCartListDialogBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lvMenu = binding.list

        itemMenuListViewModel =
            ViewModelProvider(
                requireActivity(),
                CartListViewModelFactory()
            )[CartListViewModel::class.java]

        cartListViewModel =
            ViewModelProvider(
                requireActivity(),
                CartListViewModelFactory()
            )[CartListViewModel::class.java]

        lvMenu = binding.list

        cartItemTask()

        LinearLayoutManager(context)

        cartListViewModel.canCartListOpen.observe(viewLifecycleOwner) {
            if (it == true) {
                displayCartItem(cartListViewModel.cartListItem.value)
                Toast.makeText(
                    context,
                    "カートから削除されました",
                    Toast.LENGTH_SHORT
                ).show()
                cartListViewModel.canCartListOpen.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cartItemTask() {
        cartListViewModel.onClickCartBtn()
        cartListViewModel.cartListItem.observe(viewLifecycleOwner) {
            displayCartItem(it)
        }
    }

    private fun displayCartItem(result: MutableList<Cart>?) {
        cartItemAdapter = CartListAdapter(this.context, result, this)
        lvMenu.adapter = cartItemAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clicked(cart: Cart?) {
        cartListViewModel.onClickDeleteBtn(cart)
    }
}