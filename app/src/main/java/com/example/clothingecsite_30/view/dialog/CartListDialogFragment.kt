package com.example.clothingecsite_30.view.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.databinding.FragmentCartListDialogBinding
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.util.CartListAdapter
import com.example.clothingecsite_30.viewModel.cart.CartListViewModel
import com.example.clothingecsite_30.viewModel.cart.CartListViewModelFactory

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

class CartListDialogFragment : BottomSheetDialogFragment() {

    private lateinit var itemMenuListViewModel: CartListViewModel
    private lateinit var cartListViewModel: CartListViewModel

    private var _binding: FragmentCartListDialogBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

        val recyclerView: RecyclerView = binding.list
        recyclerView.setHasFixedSize(true);



        itemMenuListViewModel.itemList.observe(viewLifecycleOwner) { items ->
            val adapter = CartListAdapter(context, items)
            recyclerView.adapter = adapter

            adapter.setOnCartItemCellClickListener(object:CartListAdapter.CartAdapterListener {
                override fun onItemClick(view: View, position: Int, item: Item) {
                    cartListViewModel.onClickDeleteItem(item);
                    Toast.makeText(context, "${item.name}がタップされました", Toast.LENGTH_LONG).show()
                }
            } )
        }

        //LayoutManagerの設定
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

//        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
//            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }
    }


    companion object {
        // TODO: Customize parameters
        fun newInstance(itemCount: Int): CartListDialogFragment =
            CartListDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}