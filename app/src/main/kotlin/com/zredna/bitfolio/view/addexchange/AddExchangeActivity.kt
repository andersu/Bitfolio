package com.zredna.bitfolio.view.addexchange

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import com.zredna.bitfolio.R
import com.zredna.bitfolio.TextChangedListener
import com.zredna.bitfolio.Exchange
import com.zredna.bitfolio.view.extensions.showKeyboard
import kotlinx.android.synthetic.main.activity_add_exchange.*
import org.koin.android.architecture.ext.viewModel

const val REQUEST_CODE_ADD_EXCHANGE = 7891

class AddExchangeActivity : AppCompatActivity() {
    private val viewModel by viewModel<AddExchangeViewModel>()

    private val selectedExchange: Exchange? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exchange)

        initView()
        bindViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_exchange_title)

        bittrexButton.setOnClickListener { viewModel.exchangeSelected(Exchange.BITTREX) }
        binanceButton.setOnClickListener { viewModel.exchangeSelected(Exchange.BINANCE) }

        apiKeyInput.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.apiKeyTextChanged(text.toString())
            }
        })

        secretInput.addTextChangedListener(object : TextChangedListener() {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.secretTextChanged(text.toString())
            }
        })

        addExchangeButton.setOnClickListener {
            val apiKey = apiKeyInput.text.toString()
            val secret = secretInput.text.toString()
            viewModel.getSelectedExchange().value?.let {
                viewModel.addExchange(it, apiKey, secret)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun bindViewModel() {
        viewModel.getSelectedExchange().observe(this, selectedExchangeObserver)
        viewModel.isAddExchangeEnabled().observe(this, addExchangeEnabledObserver)
    }

    private val selectedExchangeObserver = Observer<Exchange> {
        bittrexButton.isSelected = it == Exchange.BITTREX
        binanceButton.isSelected = it == Exchange.BINANCE

        apiExplanationText.text =
                if (it == Exchange.BITTREX)
                    getString(R.string.add_exchange_bittrex_explanation)
                else
                    getString(R.string.add_exchange_binance_explanation)
        apiExplanationText.movementMethod = LinkMovementMethod.getInstance()

        listOf(apiKeyInput, apiKeyLabel, secretInput, secretLabel, apiExplanationText).forEach {
            it.visibility = View.VISIBLE
        }

        apiKeyInput.showKeyboard(this)
    }

    private val addExchangeEnabledObserver = Observer<Boolean> {
        it?.let { addExchangeButton.isEnabled = it }
    }
}