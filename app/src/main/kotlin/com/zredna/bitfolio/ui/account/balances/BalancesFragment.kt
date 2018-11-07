package com.zredna.bitfolio.ui.account.balances

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zredna.bitfolio.db.datamodel.BalanceInBtc
import com.zredna.bitfolio.R
import com.zredna.bitfolio.repository.Resource
import com.zredna.bitfolio.repository.Status
import com.zredna.bitfolio.ui.account.exchanges.addexchange.REQUEST_CODE_ADD_EXCHANGE
import kotlinx.android.synthetic.main.fragment_balances.*
import observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.snackbar.Snackbar


class BalancesFragment : Fragment() {
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
        if (requestCode == REQUEST_CODE_ADD_EXCHANGE && resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.refresh()
        }
    }

    private fun initView() {
        recyclerViewBalances.adapter = balancesAdapter
        recyclerViewBalances.layoutManager = LinearLayoutManager(activity)

        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    // region Observe view model
    private fun bindViewModel() {
        observe(viewModel.balances, ::onBalancesUpdated)
        observe(viewModel.totalBalance, ::onTotalBalanceUpdated)
    }

    private fun onBalancesUpdated(balancesResource: Resource<List<BalanceInBtc>>?) {
        when (balancesResource?.status) {
            Status.LOADING -> {
                showLoading()
            }
            Status.SUCCESS -> {
                hideLoading()
                showBalances(balancesResource.data)
            }
            Status.ERROR -> {
                hideLoading()
                showError()
            }
        }
    }

    private fun onTotalBalanceUpdated(totalBalance: Double?) {
        totalBalance?.let { textViewTotalBalance.text = "฿ $it" }
    }
    // endregion

    // region Helper methods
    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
        view?.isEnabled = false
        view?.alpha = 0.2f
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        view?.isEnabled = true
        view?.alpha = 1f
    }

    private fun showBalances(balances: List<BalanceInBtc>?) {
        balancesAdapter.balances = balances?.filter { balanceInBtc ->
            balanceInBtc.balanceInBtc > 0.0001
        } ?: emptyList()
    }

    private fun showError() {
        Snackbar.make(
                coordinatorLayout,
                R.string.get_balances_error,
                Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            viewModel.refresh()
        }.show()
    }
    // endregion
}