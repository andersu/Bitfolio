package com.zredna.bitfolio.account

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.zredna.bitfolio.R
import kotlinx.android.synthetic.main.activity_account.*
import org.koin.android.architecture.ext.viewModel

class AccountActivity : AppCompatActivity() {
    private val accountViewModel by viewModel<AccountViewModel>()

    private val balancesAdapter = BalancesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initView()

        accountViewModel.balances.observe(this, Observer {
            it?.data?.let {
                swipeRefreshLayout.isRefreshing = false
                enableBalanceList()
                balancesAdapter.balances = it
            }
        })

        //presenter.onCreate()
    }

    private fun initView() {
        recyclerViewBalances.adapter = balancesAdapter
        recyclerViewBalances.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            disableBalanceList()
            accountViewModel.refresh()

        }
    }

    fun showCouldNotGetBalances() {

    }

    fun showTotalBalance(totalBalance: Double) {
        textViewTotalBalance.text = "฿ $totalBalance"
    }

    private fun disableBalanceList() {
        recyclerViewBalances.isEnabled = false
        recyclerViewBalances.alpha = 0.2f
    }

    private fun enableBalanceList() {
        recyclerViewBalances.isEnabled = true
        recyclerViewBalances.alpha = 1f
    }
}
