package com.example.clothingecsite_30.util

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.model.Cart
import com.google.android.material.imageview.ShapeableImageView

// interfaceの実装
interface CartAdapterListener {
    fun clicked(cart: Cart?)
}

/**
 * カートリストアダプタ
 */
class CartListAdapter(
    context: Context?,
    var itemList: MutableList<Cart>?,
    private val listener: CartAdapterListener
) : ArrayAdapter<Cart>(
    context!!, 0,
    itemList!!
) {

    private val layoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // cartItemsの取得
        val cartItems = itemList?.get(position)

        // レイアウトの設定
        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.fragment_cart_list_dialog_item, parent, false)
        }

        // 各Viewの設定
        val imageView = view?.findViewById<ShapeableImageView>(R.id.cart_img_path)
        if (cartItems != null) {
            var imgPath = context.resources.getIdentifier(
                cartItems.imgPath,
                "drawable",
                context.packageName
            )
            imageView?.setImageResource(imgPath)
        }

        val name = view?.findViewById<TextView>(R.id.cart_item_name)
        name?.text = cartItems?.name

        val totalPrice = view?.findViewById<TextView>(R.id.total_price)
        totalPrice?.text = "¥${"%,d".format(cartItems?.totalPrice)}"

        view?.findViewById<TextView>(R.id.deleteBtn)?.setOnClickListener {
            listener.clicked(cartItems)
        }
        return view!!
    }
}