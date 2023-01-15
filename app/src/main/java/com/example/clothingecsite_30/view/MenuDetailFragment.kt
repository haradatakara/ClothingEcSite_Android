package com.example.clothingecsite_30.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentMenuDetailBinding
import com.example.clothingecsite_30.viewModel.item.ItemModelFactory
import com.example.clothingecsite_30.viewModel.item.ItemViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MenuDetailFragment : Fragment(){

    private lateinit var itemViewModel: ItemViewModel

    private var _binding: FragmentMenuDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuDetailBinding.inflate(inflater, container, false)

        Firebase.auth.uid
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemViewModel =
            ViewModelProvider(requireActivity(), ItemModelFactory())[ItemViewModel::class.java]

        val itemPath = binding.detailImage
        val itemName = binding.detailName
        val itemPrice = binding.priceNum
        val addCartBtn = binding.gotoCartBtn
        val purchaseBtn = binding.gotoPurchase
        val itemDetail = itemViewModel.item.value

        itemName.text = itemDetail?.name
        itemPrice.text =  "¥${" %, d".format(itemDetail?.price)}"
        itemPath.setImageResource(
            resources.getIdentifier(itemDetail?.imgPath, "drawable", activity?.packageName)
        )
        purchaseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_PurchaseRegisterIndividualFragment_to_MenuListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}









