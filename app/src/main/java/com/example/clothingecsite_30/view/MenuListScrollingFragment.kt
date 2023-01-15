package com.example.clothingecsite_30.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentMenuListScrollingBinding
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.util.ItemMenuListAdapter
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.item.ItemModelFactory
import com.example.clothingecsite_30.viewModel.item.ItemViewModel
import com.example.clothingecsite_30.viewModel.purchaseComfirm.ItemMenuListViewModel
import com.example.clothingecsite_30.viewModel.purchaseComfirm.ItemMenuListViewModelFactory


class MenuListScrollingFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var itemMenuListViewModel: ItemMenuListViewModel
    private lateinit var itemViewModel: ItemViewModel

    private var _binding: FragmentMenuListScrollingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuListScrollingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel =
            ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        itemViewModel =
            ViewModelProvider(requireActivity(), ItemModelFactory())[ItemViewModel::class.java]
        itemMenuListViewModel =
            ViewModelProvider(
                requireActivity(),
                ItemMenuListViewModelFactory()
            )[ItemMenuListViewModel::class.java]

        val progressBar = binding.loading

        // アイテム取得
        progressBar.visibility = View.VISIBLE
        itemMenuListViewModel.fetchItemList()

        loginViewModel.isLogout.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else if (it == true) {
                startActivity(Intent(this.requireContext(), IntroActivity::class.java))
            }
        }

        val recyclerView: RecyclerView = binding.lvMenu
        recyclerView.setHasFixedSize(true);

        //LayoutManagerの設定
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        itemMenuListViewModel.itemList.observe(viewLifecycleOwner) { items ->
            progressBar.visibility = View.GONE
            val adapter = ItemMenuListAdapter(context, items)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : ItemMenuListAdapter.OnItemClickListener {
                override fun onItemClickListener(view: View, position: Int, clickedText: Item) {
                    itemViewModel.fetchItem(clickedText.documentId)
                }
            })
        }

        itemViewModel.item.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                findNavController().navigate(R.id.action_MenuDetailFragment_to_MenuListFragment)
            }
        }
    }

    private fun Item.toMap(): MutableMap<String, *> {
        return mutableMapOf(
            "itemId" to this.itemId,
            "name" to this.name,
            "price" to "¥${"%,d".format(this.price)}",
            "imgPath" to resources.getIdentifier(this.imgPath, "drawable", activity?.packageName),
        )
    }

}