package com.example.clothingecsite_30.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.model.Item
import com.google.android.material.imageview.ShapeableImageView

// interfaceの実装
//interface CartAdapterListener {
//    fun clicked(cart: Cart?)
//}

class ItemMenuListAdapter(
    private val context: Context?,
    private val itemList: List<Item>
) : RecyclerView.Adapter<ItemMenuListAdapter.ViewHolder>() {

    // リスナー格納変数
    lateinit var listener: OnItemClickListener

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView

        init {
            name = view.findViewById(R.id.name)
        }
        val price: TextView

        init {
            price = view.findViewById(R.id.price)
        }
        val imgPath: ShapeableImageView

        init {
            imgPath = view.findViewById(R.id.imgPath)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.fragment_menu_list_item_scrolling, parent, false)
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

        viewHolder.price.text = "¥${"%,d".format(item.price)}"
        val imgPath = context?.resources?.getIdentifier(
            item.imgPath,
            "drawable",
            context.packageName
        )
        viewHolder.imgPath.setImageResource(imgPath!!)

        // タップしたとき
        viewHolder.itemView.setOnClickListener {
            listener.onItemClickListener(it, position, itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: Item)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}