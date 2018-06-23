package com.zredna.bitfolio.view.account.exchanges

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zredna.bitfolio.R
import kotlinx.android.synthetic.main.fragment_exchanges.*
import org.koin.android.architecture.ext.viewModel

class ExchangesFragment: Fragment() {
    private val viewModel by viewModel<ExchangesViewModel>()

    private val exchangesAdapter = ExchangesAdapter()

    lateinit var onAddExchangeClicked: () -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exchanges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindViewModel()
    }

    private fun initView() {
        recyclerViewExchanges.adapter = exchangesAdapter
        recyclerViewExchanges.layoutManager = LinearLayoutManager(activity)

        addExchangeButton.setOnClickListener { onAddExchangeClicked() }
    }

    private fun bindViewModel() {
        viewModel.exchangeCredentials.observe(this, Observer {
            it?.let {
                exchangesAdapter.exchangeCredentials = it
            }
        })
    }
}
