package com.zredna.bitfolio.ui.addexchange

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.R
import com.zredna.bitfolio.TextChangedListener
import com.zredna.bitfolio.ui.extensions.showKeyboard
import kotlinx.android.synthetic.main.activity_add_exchange.*
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
            viewModel.getSelectedExchange().value?.let { exchange ->
                viewModel.addExchange(exchange, apiKey, secret)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }
    }

    private fun bindViewModel() {
        viewModel.getSelectedExchange().observe(this, selectedExchangeObserver)
        viewModel.isAddExchangeEnabled().observe(this, addExchangeEnabledObserver)
    }

    private val selectedExchangeObserver = Observer<ExchangeName> {
        bittrexButton.isSelected = it == ExchangeName.BITTREX
        binanceButton.isSelected = it == ExchangeName.BINANCE

        apiExplanationText.text =
                if (it == ExchangeName.BITTREX)
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

    private val addExchangeEnabledObserver = Observer<Boolean> {
        it?.let { isEnabled -> addExchangeButton.isEnabled = isEnabled }
    }
}