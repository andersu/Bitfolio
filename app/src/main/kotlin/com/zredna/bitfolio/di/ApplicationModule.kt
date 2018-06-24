package com.zredna.bitfolio.di

import android.arch.persistence.room.Room
import android.content.Context
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.binanceapiclient.BinanceCredentials
import com.zredna.binanceapiclient.BinanceCredentialsProvider
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.BuildConfig.DEBUG
import com.zredna.bitfolio.ExchangeName
import com.zredna.bitfolio.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.db.BitfolioDatabase
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.ExchangeRepository
import com.zredna.bitfolio.view.account.balances.BalancesViewModel
import com.zredna.bitfolio.view.account.exchanges.ExchangesViewModel
import com.zredna.bitfolio.view.addexchange.AddExchangeViewModel
import com.zredna.bittrex.apiclient.BittrexApiClient
import com.zredna.bittrex.apiclient.BittrexCredentials
import com.zredna.bittrex.apiclient.BittrexCredentialsProvider
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val bitfolioModule: Module = applicationContext {
    viewModel { BalancesViewModel(get()) }
    viewModel { ExchangesViewModel(get()) }
    viewModel { AddExchangeViewModel(get()) }

    bean { androidApplication().getSharedPreferences("bitfolio", Context.MODE_PRIVATE) }
    bean { BalanceRepository(get(), get(), get(), get(), get(), get(), get(), get()) }
    bean { ExchangeRepository(get(), get()) }

    bean { BtcBalanceCalculator() }

    bean {
        Room.databaseBuilder(
                androidApplication(),
                BitfolioDatabase::class.java,
                "bitfolio.db"
        ).build()
    }

    bean { get<BitfolioDatabase>().balanceDao() }
    bean { get<BitfolioDatabase>().exchangeDao() }

    bean {
        val exchangeCredentialsRepository = get<ExchangeRepository>()

        val builder = BittrexApiClient.Builder()
        if (DEBUG) {
            builder.setLoggingEnabled()
        }
        builder.build(object : BittrexCredentialsProvider {
            override fun getCredentials(): BittrexCredentials {
                val exchangeCredentials = exchangeCredentialsRepository
                        .getCredentialsForExchange(ExchangeName.BITTREX.name)
                return BittrexCredentials(exchangeCredentials.apiKey, exchangeCredentials.secret)
            }
        })
    }
    bean { BittrexBalanceDtoConverter() }
    bean { BittrexMarketSummaryDtoConverter() }

    bean {
        val exchangeCredentialsRepository = get<ExchangeRepository>()
        val builder = BinanceApiClient.Builder()
        if (DEBUG) {
            builder.setLoggingEnabled()
        }
        builder.build(object : BinanceCredentialsProvider {
            override fun getCredentials(): BinanceCredentials {
                val exchangeCredentials = exchangeCredentialsRepository
                        .getCredentialsForExchange(ExchangeName.BINANCE.name)
                return BinanceCredentials(exchangeCredentials.apiKey, exchangeCredentials.secret)
            }
        })
    }
    bean { BinanceBalanceDtoConverter() }
    bean { BinanceMarketSummaryDtoConverter() }
}