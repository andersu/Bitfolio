package com.zredna.bitfolio.view.account

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.zredna.bitfolio.R
import com.zredna.bitfolio.BalanceInBtc
import com.zredna.bitfolio.BalancesAdapter
import kotlinx.android.synthetic.main.activity_account.*
import org.koin.android.architecture.ext.viewModel

class AccountActivity : AppCompatActivity() {
    private val viewModel by viewModel<AccountViewModel>()

    private val balancesAdapter = BalancesAdapter()

    // region Setup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initView()
        bindViewModel()
    }

    private fun initView() {
        recyclerViewBalances.adapter = balancesAdapter
        recyclerViewBalances.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun bindViewModel() {
        viewModel.balances.observe(this, balanceListObserver)

        viewModel.totalBalance.observe(this, totalBalanceObserver)
        viewModel.isRefreshing().observe(this, refreshingObserver)
    }
    // endregion

    // region Observers
    private val balanceListObserver = Observer<List<BalanceInBtc>> {
        it?.let {
            balancesAdapter.balances = it
            viewModel.balancesUpdated()
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
