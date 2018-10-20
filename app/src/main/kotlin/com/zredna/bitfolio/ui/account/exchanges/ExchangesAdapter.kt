package com.zredna.bitfolio.ui.account.exchanges

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zredna.bitfolio.domain.model.ExchangeCredentials

class ExchangesAdapter : RecyclerView.Adapter<ExchangesAdapter.ExchangeViewHolder>() {

    var exchangeCredentials: List<ExchangeCredentials> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onDeleteClick: ((ExchangeCredentials) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        return ExchangeViewHolder(ExchangeItemView(parent.context))
    }

    override fun getItemCount(): Int {
        return exchangeCredentials.count()
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.exchangeItemView.bind(
                exchangeCredentials[position],
                onDeleteClick = {
                    onDeleteClick?.invoke(exchangeCredentials[position])
                    notifyItemRemoved(position)
                }
        )
    }

    inner class ExchangeViewHolder(
            val exchangeItemView: ExchangeItemView
    ) : RecyclerView.ViewHolder(exchangeItemView)
}