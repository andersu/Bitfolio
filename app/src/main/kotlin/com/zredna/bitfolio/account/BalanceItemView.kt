package com.zredna.bitfolio.account

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zredna.bitfolio.R
import kotlinx.android.synthetic.main.item_view_balance.view.*

class BalanceItemView @JvmOverloads constructor(
        context: Context, attrs:
        AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.item_view_balance, this, true)
    }

    fun bind(balanceInBtc: BalanceInBtc) {
        textViewCurrency.text = balanceInBtc.currency
        textViewBalance.text = "฿ ${balanceInBtc.balanceInBtc}"
    }
}