package com.zredna.bitfolio

import android.text.Editable
import android.text.TextWatcher

/**
 * Interface that by default implements all of TextWatchers methods.
 * Using this interface allows for just implementing the methods needed in the use-case.
 */
abstract class TextChangedListener: TextWatcher {
    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) { }
    override fun afterTextChanged(editable: Editable?) { }
    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {}
}