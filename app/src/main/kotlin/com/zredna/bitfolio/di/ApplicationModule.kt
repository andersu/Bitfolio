package com.zredna.bitfolio.di

import android.content.Context
import androidx.room.Room
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.binanceapiclient.BinanceCredentials
import com.zredna.binanceapiclient.BinanceCredentialsProvider
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.BuildConfig.DEBUG
import com.zredna.bitfolio.domain.model.ExchangeName
import com.zredna.bitfolio.domain.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.domain.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.domain.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.domain.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.db.BitfolioDatabase
import com.zredna.bitfolio.domain.DeleteExchangeUseCase
import com.zredna.bitfolio.domain.GetBalancesUseCase
import com.zredna.bitfolio.domain.GetExchangeCredentialsUseCase
import com.zredna.bitfolio.domain.GetExchangesUseCase
import com.zredna.bitfolio.domain.SaveExchangeUseCase
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.repository.ExchangeRepository
import com.zredna.bitfolio.service.BinanceService
import com.zredna.bitfolio.service.BittrexService
import com.zredna.bitfolio.ui.account.balances.BalancesViewModel
import com.zredna.bitfolio.ui.account.exchanges.ExchangesViewModel
import com.zredna.bitfolio.ui.account.exchanges.addexchange.AddExchangeViewModel
import com.zredna.bittrex.apiclient.BittrexApiClient
import com.zredna.bittrex.apiclient.BittrexCredentials
import com.zredna.bittrex.apiclient.BittrexCredentialsProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val bitfolioModule: Module = module {
    // ViewModels
    viewModel { BalancesViewModel(get()) }
    viewModel { ExchangesViewModel(get(), get(), get()) }
    viewModel { AddExchangeViewModel(get()) }

    // UseCases
    single { DeleteExchangeUseCase(get()) }
    single { GetBalancesUseCase(get()) }
    single { GetExchangeCredentialsUseCase(get()) }
    single { GetExchangesUseCase(get()) }
    single { SaveExchangeUseCase(get()) }

    // Repositories
    single { BalanceRepository(get(), get(), get(), get(), get()) }
    single { ExchangeRepository(get(), get()) }

    // SharedPreferences
    single { androidApplication().getSharedPreferences("bitfolio", Context.MODE_PRIVATE) }

    single { BtcBalanceCalculator() }

    // Database
    single {
        Room.databaseBuilder(
                androidApplication(),
                BitfolioDatabase::class.java,
                "bitfolio.db"
        ).build()
    }

    // Daos
    single { get<BitfolioDatabase>().balanceDao() }
    single { get<BitfolioDatabase>().exchangeDao() }

    single {
        val exchangeCredentialsRepository = get<ExchangeRepository>()

        val builder = BittrexApiClient.Builder()
        if (DEBUG) {
            builder.setLoggingEnabled()
        }
        builder.build(object : BittrexCredentialsProvider {
            override fun getCredentials(): BittrexCredentials {
                val exchangeCredentials = exchangeCredentialsRepository
                        .getCredentialsForExchange(ExchangeName.BITTREX)
                return BittrexCredentials(exchangeCredentials.apiKey, exchangeCredentials.secret)
            }
        })
    }
    single { BittrexService(get(), get(), get()) }
    single { BittrexBalanceDtoConverter() }
    single { BittrexMarketSummaryDtoConverter() }

    single {
        val exchangeCredentialsRepository = get<ExchangeRepository>()
        val builder = BinanceApiClient.Builder()
        if (DEBUG) {
            builder.setLoggingEnabled()
        }
        builder.build(object : BinanceCredentialsProvider {
            override fun getCredentials(): BinanceCredentials {
                val exchangeCredentials = exchangeCredentialsRepository
                        .getCredentialsForExchange(ExchangeName.BINANCE)
                return BinanceCredentials(exchangeCredentials.apiKey, exchangeCredentials.secret)
            }
        })
    }

    single { BinanceService(get(), get(), get()) }
    single { BinanceBalanceDtoConverter() }
    single { BinanceMarketSummaryDtoConverter() }
}