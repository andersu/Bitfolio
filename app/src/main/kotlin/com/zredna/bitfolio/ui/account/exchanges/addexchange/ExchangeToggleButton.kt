package com.zredna.bitfolio.ui.account.exchanges.addexchange

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.zredna.bitfolio.R
import kotlinx.android.synthetic.main.view_exchange_toggle_button.view.*

class ExchangeToggleButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_exchange_toggle_button, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExchangeToggleButton)
        val text = attributes.getText(R.styleable.ExchangeToggleButton_android_text)
        exchangeToggleButton.text = text
        attributes.recycle()
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        exchangeToggleButton.setOnClickListener(onClickListener)
    }
}