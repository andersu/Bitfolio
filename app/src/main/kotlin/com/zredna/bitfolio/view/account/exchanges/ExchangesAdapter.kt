package com.zredna.bitfolio.view.account.exchanges

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
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
        holder.exchangeItemView.bind(exchangeCredentials.get(position))
    }

    inner class ExchangeViewHolder(
            val exchangeItemView: ExchangeItemView
    ): RecyclerView.ViewHolder(exchangeItemView)
}