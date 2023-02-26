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
import com.example.clothingecsite_30.enum.ItemGenre
import com.example.clothingecsite_30.model.Item
import com.example.clothingecsite_30.util.ItemMenuListAdapter
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.item.ItemModelFactory
import com.example.clothingecsite_30.viewModel.item.ItemViewModel
import com.example.clothingecsite_30.viewModel.menu.ItemMenuListViewModel
import com.example.clothingecsite_30.viewModel.menu.ItemMenuListViewModelFactory

/**
 * メニューリストのフラグメント
 */
class MenuListScrollingFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var itemMenuListViewModel: ItemMenuListViewModel
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var recyclerView: RecyclerView

    private var _binding: FragmentMenuListScrollingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
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

        //ログアウトボタン押下時
        loginViewModel.isLogout.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else if (it == true) {
                startActivity(Intent(this.requireContext(), IntroActivity::class.java))
            }
        }

        recyclerView = binding.lvMenu
        recyclerView.setHasFixedSize(true)

        //LayoutManagerの設定
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        var allItems: List<Item>? = null

        /**
         * メニューリスト表示
         */
        itemMenuListViewModel.itemList.observe(viewLifecycleOwner) { items ->
            //取得できなかったら
            if(items.isEmpty()) {
                binding.oneMoreBtn.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } else {
                allItems = items
                createAdapter(allItems)
                progressBar.visibility = View.GONE
            }
        }

        binding.btTops.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Thread.sleep(1000)
            val tops = allItems?.filter {
                it.genre.lowercase() == ItemGenre.TOPS.value
            }
            createAdapter(tops)
            progressBar.visibility = View.GONE
        }

        binding.btPants.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Thread.sleep(1000)
            val pants = allItems?.filter {
                it.genre.lowercase() == ItemGenre.PANTS.value
            }
            createAdapter(pants)
            progressBar.visibility = View.GONE
        }

        binding.btAll.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            createAdapter(allItems)
            progressBar.visibility = View.GONE
        }

        binding.oneMoreBtn.setOnClickListener {
            binding.oneMoreBtn.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            itemMenuListViewModel.fetchItemList()
        }

        /**
         * 商品詳細ページの移動できるか
         */
        itemViewModel.item.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                findNavController().navigate(R.id.action_MenuDetailFragment_to_MenuListFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        itemMenuListViewModel.fetchItemList()
    }

    private fun createAdapter(pants: List<Item>?) {
        val adapter = ItemMenuListAdapter(context, pants!!)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ItemMenuListAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, clickedText: Item) {
                itemViewModel.fetchItem(clickedText.documentId)
            }
        })
    }

}