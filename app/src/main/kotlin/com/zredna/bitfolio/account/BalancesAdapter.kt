package com.zredna.bitfolio.account

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class BalancesAdapter: RecyclerView.Adapter<BalancesAdapter.BalanceViewHolder>() {

    var balancesInBtc: List<BalanceInBtc> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: BalancesAdapter.BalanceViewHolder, position: Int) {
        holder.balanceItemView.bind(balancesInBtc.get(position))
    }

    override fun getItemCount(): Int {
        return balancesInBtc.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalancesAdapter.BalanceViewHolder {
        return BalanceViewHolder(BalanceItemView(parent.context))
    }

    inner class BalanceViewHolder(
            val balanceItemView: BalanceItemView
    ): RecyclerView.ViewHolder(balanceItemView)
}