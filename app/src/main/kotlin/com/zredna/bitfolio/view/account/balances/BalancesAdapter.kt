package com.zredna.bitfolio.view.account.balances

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zredna.bitfolio.db.datamodel.BalanceInBtc

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