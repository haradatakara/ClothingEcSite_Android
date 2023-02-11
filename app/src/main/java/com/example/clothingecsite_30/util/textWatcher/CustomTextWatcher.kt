package com.example.clothingecsite_30.util.textWatcher

import android.text.Editable
import android.text.TextWatcher
import android.view.View

/**
 * 入力エラー判定に関するクラス
 */
class CustomTextWatcher(val view: View, val listener: CustomTextWatcherListener): TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        listener.beforeTextChanged(view, p0, p1, p2, p3)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        listener.onTextChanged(view, p0, p1, p2, p3)
    }

    override fun afterTextChanged(p0: Editable?) {
        listener.afterTextChanged(view, p0)
    }
}