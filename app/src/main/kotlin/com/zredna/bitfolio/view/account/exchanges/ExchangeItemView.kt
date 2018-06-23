package com.zredna.bitfolio.view.account.exchanges

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zredna.bitfolio.R
import com.zredna.bitfolio.model.ExchangeCredentials
import kotlinx.android.synthetic.main.item_view_exchange.view.*

class ExchangeItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.item_view_exchange, this, true)
    }

    fun bind(exchangeCredentials: ExchangeCredentials) {
        textViewName.text = exchangeCredentials.name
    }
}
