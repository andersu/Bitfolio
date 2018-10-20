package com.zredna.bitfolio.ui.account.exchanges

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.zredna.bitfolio.R
import com.zredna.bitfolio.domain.model.ExchangeCredentials
import kotlinx.android.synthetic.main.item_view_exchange.view.*

class ExchangeItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.item_view_exchange, this)
    }

    fun bind(exchangeCredentials: ExchangeCredentials, onDeleteClick: () -> Unit) {
        textViewName.text = exchangeCredentials.name.name

        imageView.setOnClickListener { onDeleteClick() }
    }
}
