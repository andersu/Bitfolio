package com.zredna.bitfolio.view.account

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.zredna.bitfolio.R
import com.zredna.bitfolio.BalanceInBtc
import com.zredna.bitfolio.BalancesAdapter
import com.zredna.bitfolio.view.addexchange.AddExchangeActivity
import com.zredna.bitfolio.view.addexchange.REQUEST_CODE_ADD_EXCHANGE
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_account, menu)
        menu.findItem(R.id.action_add_exchange).setOnMenuItemClickListener {
            startActivityForResult(
                    Intent(this, AddExchangeActivity::class.java),
                    REQUEST_CODE_ADD_EXCHANGE
            )

            true
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_EXCHANGE && resultCode == Activity.RESULT_OK) {
            viewModel.refresh()
        }
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
