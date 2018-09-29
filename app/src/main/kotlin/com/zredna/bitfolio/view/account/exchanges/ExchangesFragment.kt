package com.zredna.bitfolio.view.account.exchanges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zredna.bitfolio.R
import kotlinx.android.synthetic.main.fragment_exchanges.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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
