package com.zredna.bitfolio.ui.account.exchanges.addexchange

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zredna.bitfolio.domain.model.ExchangeName
import com.zredna.bitfolio.R
import com.zredna.bitfolio.TextChangedListener
import com.zredna.bitfolio.ui.extensions.showKeyboard
import kotlinx.android.synthetic.main.activity_add_exchange.*
import observe
import org.koin.androidx.viewmodel.ext.android.viewModel

const val REQUEST_CODE_ADD_EXCHANGE = 7891

class AddExchangeActivity : AppCompatActivity() {
    private val viewModel by viewModel<AddExchangeViewModel>()

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

        bittrexButton.setOnClickListener { viewModel.exchangeSelected(ExchangeName.BITTREX) }
        binanceButton.setOnClickListener { viewModel.exchangeSelected(ExchangeName.BINANCE) }

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
            viewModel.selectedExchange.value?.let { exchange ->
                viewModel.addExchange(exchange, apiKey, secret)
                finish()
            }
        }
    }

    private fun bindViewModel() {
        observe(viewModel.selectedExchange, ::onSelectedExchangeUpdated)
        observe(viewModel.isAddExchangeEnabled, ::onAddExchangeEnabledUpdated)
    }

    private fun onSelectedExchangeUpdated(exchangeName: ExchangeName?) {
        bittrexButton.isSelected = exchangeName == ExchangeName.BITTREX
        binanceButton.isSelected = exchangeName == ExchangeName.BINANCE

        apiExplanationText.text =
                if (exchangeName == ExchangeName.BITTREX)
                    getString(R.string.add_exchange_bittrex_explanation)
                else
                    getString(R.string.add_exchange_binance_explanation)
        apiExplanationText.movementMethod = LinkMovementMethod.getInstance()

        listOf(apiKeyInput, apiKeyLabel, secretInput, secretLabel, apiExplanationText)
                .forEach { textView ->
                    textView.visibility = View.VISIBLE
                }

        apiKeyInput.showKeyboard(this)
    }

    private fun onAddExchangeEnabledUpdated(isAddExchangeEnabled: Boolean?) {
        isAddExchangeEnabled?.let { isEnabled -> addExchangeButton.isEnabled = isEnabled }
    }
}