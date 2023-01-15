package com.example.clothingecsite_30.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.model.Item
import com.google.android.material.imageview.ShapeableImageView

// interfaceの実装


class CartListAdapter(
    private val context: Context?,
    private val itemList: List<Item>
) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    // 1. リスナを格納する変数を定義（lateinitで初期化を遅らせている）
    private lateinit var listener: CartAdapterListener

    // 2. インターフェースを作成
    interface CartAdapterListener {
        fun onItemClick(view: View, position: Int, item: Item)
    }

    // 3. リスナーをセット
    fun setOnCartItemCellClickListener(listener: CartAdapterListener) {
        // 定義した変数listenerに実行したい処理を引数で渡す（BookListFragmentで渡している）
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView

        init {
            name = view.findViewById(R.id.cart_item_name)
        }

        val totalPrice: TextView

        init {
            totalPrice = view.findViewById(R.id.total_price)
        }

        val imgPath: ShapeableImageView

        init {
            imgPath = view.findViewById(R.id.cart_img_path)
        }

        val purchaseNum: TextView

        init {
            purchaseNum = view.findViewById(R.id.purchase_num)
        }

        val deleteBtn: Button

        init {
            deleteBtn = view.findViewById(R.id.deleteBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.fragment_cart_list_dialog_item, parent, false)
        view.setOnClickListener {
            Toast.makeText(
                context,
                "Click Pos=",
                Toast.LENGTH_LONG
            ).show()
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = itemList[position]
        viewHolder.name.text = item.name
        viewHolder.totalPrice.text = "¥${"%,d".format(item.price)}"
        val imgPath = context?.resources?.getIdentifier(
            item.imgPath,
            "drawable",
            context.packageName
        )
        viewHolder.imgPath.setImageResource(imgPath!!)
        viewHolder.purchaseNum.text = "2個";

        viewHolder.deleteBtn.setOnClickListener {
            listener.onItemClick(it, position, itemList[position])
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}