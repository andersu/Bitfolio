package com.zredna.bitfolio.ui.account.exchanges

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zredna.bitfolio.model.ExchangeCredentials

class ExchangesAdapter: RecyclerView.Adapter<ExchangesAdapter.ExchangeViewHolder>() {

    var exchangeCredentials: List<ExchangeCredentials> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        return ExchangeViewHolder(ExchangeItemView(parent.context))
    }

    override fun getItemCount(): Int {
        return exchangeCredentials.count()
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.exchangeItemView.bind(exchangeCredentials[position])
    }

    inner class ExchangeViewHolder(
            val exchangeItemView: ExchangeItemView
    ): RecyclerView.ViewHolder(exchangeItemView)
}