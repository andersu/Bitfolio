package com.zredna.bitfolio.view.addexchange

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zredna.bitfolio.R
import com.zredna.bitfolio.R.id.apiKeyInput
import com.zredna.bitfolio.TextChangedListener
import com.zredna.bitfolio.view.extensions.showKeyboard
import kotlinx.android.synthetic.main.activity_add_exchange.*
import org.koin.android.architecture.ext.viewModel

class AddExchangeActivity : AppCompatActivity() {
    private val viewModel by viewModel<AddExchangeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exchange)

        initView()
        bindViewModel()
    }

    private fun initView() {
        bittrexButton.setOnClickListener { viewModel.exchangeSelected(Exchange.BITTREX) }
        binanceButton.setOnClickListener { viewModel.exchangeSelected(Exchange.BINANCE) }

        apiKeyInput.addTextChangedListener(object: TextChangedListener() {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.apiKeyTextChanged(text.toString())
            }
        })

        secretInput.addTextChangedListener(object: TextChangedListener() {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.secretTextChanged(text.toString())
            }
        })
    }

    private fun bindViewModel() {
        viewModel.getSelectedExchange().observe(this, selectedExchangeObserver)
        viewModel.isAddExchangeEnabled().observe(this, addExchangeEnabledObserver)
    }

    private val selectedExchangeObserver = Observer<Exchange> {
        bittrexButton.isSelected = it == Exchange.BITTREX
        binanceButton.isSelected = it == Exchange.BINANCE

        listOf(apiKeyInput, apiKeyLabel, secretInput, secretLabel, apiExplanationText).forEach {
            it.visibility = View.VISIBLE
        }

        apiKeyInput.showKeyboard(this)
    }

    private val addExchangeEnabledObserver = Observer<Boolean> {
        it?.let { addExchangeButton.isEnabled = it }
    }
}