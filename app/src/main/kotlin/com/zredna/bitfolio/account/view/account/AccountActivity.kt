package com.zredna.bitfolio.account.view.account

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.zredna.bitfolio.R
import com.zredna.bitfolio.account.BalanceInBtc
import com.zredna.bitfolio.account.BalancesAdapter
import kotlinx.android.synthetic.main.activity_account.*
import org.koin.android.architecture.ext.viewModel

class AccountActivity : AppCompatActivity() {
    private val accountViewModel by viewModel<AccountViewModel>()

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

        swipeRefreshLayout.setOnRefreshListener { accountViewModel.refresh() }
    }

    private fun bindViewModel() {
        accountViewModel.balances.observe(this, balanceListObserver)

        accountViewModel.totalBalance.observe(this, totalBalanceObserver)
        accountViewModel.isRefreshing().observe(this, refreshingObserver)
    }
    // endregion

    // region Observers
    private val balanceListObserver = Observer<List<BalanceInBtc>> {
        it?.let {
            balancesAdapter.balances = it
            accountViewModel.balancesUpdated()
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
