package com.zredna.bitfolio

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class BalancesAdapter: RecyclerView.Adapter<BalancesAdapter.BalanceViewHolder>() {

    var balances: List<BalanceInBtc> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.balanceItemView.bind(balances.get(position))
    }

    override fun getItemCount(): Int {
        return balances.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(BalanceItemView(parent.context))
    }

    inner class BalanceViewHolder(
            val balanceItemView: BalanceItemView
    ): RecyclerView.ViewHolder(balanceItemView)
}