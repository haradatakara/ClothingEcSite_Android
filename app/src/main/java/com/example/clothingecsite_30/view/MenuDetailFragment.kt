package com.example.clothingecsite_30.view

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentMenuDetailBinding
import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.view.dialog.CartListDialogFragment
import com.example.clothingecsite_30.viewModel.cart.CartListViewModel
import com.example.clothingecsite_30.viewModel.cart.CartListViewModelFactory
import com.example.clothingecsite_30.viewModel.item.ItemModelFactory
import com.example.clothingecsite_30.viewModel.item.ItemViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * 商品詳細ページのフラグメント
 */
class MenuDetailFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var cartListViewModel: CartListViewModel

    private var _binding: FragmentMenuDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemViewModel =
            ViewModelProvider(requireActivity(), ItemModelFactory())[ItemViewModel::class.java]
        cartListViewModel = ViewModelProvider(requireActivity(), CartListViewModelFactory())[CartListViewModel::class.java]

        val itemPath = binding.detailImage
        val itemName = binding.detailName
        val itemPrice = binding.priceNum
        val addCartBtn = binding.gotoCartBtn
        val purchaseBtn = binding.gotoPurchase
        val returnBtn = binding.returnButton
        val itemDetail = itemViewModel.item.value

        itemName.text = itemDetail?.name
        itemPrice.text = "¥${" %, d".format(itemDetail?.price)}"
        itemPath.setImageResource(
            resources.getIdentifier(itemDetail?.imgPath, "drawable", activity?.packageName)
        )

        //購入ボタン押下
        purchaseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_PurchaseRegisterIndividualFragment_to_MenuListFragment)
        }

        //戻るボタン押下
        returnBtn.setOnClickListener {
            itemViewModel._item.value = null
            findNavController().navigate(R.id.action_MenuListFragment_to_MenuDetailFragment)
        }

        //カート追加ボタン押下
        addCartBtn.setOnClickListener {
            cartListViewModel.cartItem.value = Cart(
                itemDetail!!.itemId.toLong(),
                itemName.text.toString(),
                itemDetail.price.toLong(),
                itemDetail.imgPath,
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                    .format(LocalDateTime.now()),
            )
            cartListViewModel.onClickAddCartBtn()
            val dialog = CartListDialogFragment()
            dialog.show(this.childFragmentManager, dialog.tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}









