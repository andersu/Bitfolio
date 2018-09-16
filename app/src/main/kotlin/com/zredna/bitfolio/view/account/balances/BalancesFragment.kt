package com.zredna.bitfolio.view.account.balances

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.R
import com.zredna.bitfolio.repository.Resource
import com.zredna.bitfolio.repository.Status
import com.zredna.bitfolio.view.addexchange.REQUEST_CODE_ADD_EXCHANGE
import kotlinx.android.synthetic.main.fragment_balances.*
import org.koin.android.architecture.ext.viewModel

class BalancesFragment: Fragment() {
    private val viewModel by viewModel<BalancesViewModel>()

    private val balancesAdapter = BalancesAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_balances, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_EXCHANGE && resultCode == Activity.RESULT_OK) {
            viewModel.refresh()
        }
    }

    private fun initView() {
        recyclerViewBalances.adapter = balancesAdapter
        recyclerViewBalances.layoutManager = LinearLayoutManager(activity)

        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun bindViewModel() {
        viewModel.balances.observe(this, balancesResourceObserver)

        viewModel.totalBalance.observe(this, totalBalanceObserver)
        viewModel.isRefreshing().observe(this, refreshingObserver)
    }

    // region Observers
    private val balancesResourceObserver = Observer<Resource<List<BalanceInBtc>>> {
        when (it?.status) {
            Status.LOADING -> { }
            Status.SUCCESS -> {
                balancesAdapter.balances = it.data?.filter {
                    balanceInBtc -> balanceInBtc.balanceInBtc > 0.0001
                } ?: emptyList()
                viewModel.balancesUpdated()
            }
            Status.ERROR -> { }
        }
    }

    private val totalBalanceObserver = Observer<Double> {
        it?.let { textViewTotalBalance.text = "฿ $it" }
    }

    private val refreshingObserver = Observer<Boolean> {
        it?.let {
            swipeRefreshLayout.isRefreshing = it
            if (it) disableBalanceList() else enableBalanceList()
        }
    }
    // endregion

    // region Helper methods
    private fun disableBalanceList() {
        recyclerViewBalances.isEnabled = false
        recyclerViewBalances.alpha = 0.2f
    }

    private fun enableBalanceList() {
        recyclerViewBalances.isEnabled = true
        recyclerViewBalances.alpha = 1f
    }
    // endregion
}