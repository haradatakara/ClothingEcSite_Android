package com.example.clothingecsite_30.util.textWatcher

import android.text.Editable
import android.view.View

interface CustomTextWatcherListener {

    fun afterTextChanged(view: View, p0: Editable?)

    fun beforeTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int)

    fun onTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int)
}